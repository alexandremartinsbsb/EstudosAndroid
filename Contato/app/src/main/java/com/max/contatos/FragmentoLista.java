package com.max.contatos;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FragmentoLista extends Fragment {

    public interface OnListaSelecionada {
        public void onListaSelecionada(Contato contato);
    }

    private OnListaSelecionada mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_lista, container, Boolean.FALSE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        List<Contato> contatos = getContatos();
        ArrayAdapter<Contato> adapterContato = new ArrayAdapter<Contato>(getActivity(), android.R.layout.simple_list_item_1, contatos);

        ListView listViewContato = (ListView) getView().findViewById(R.id.listViewContatos);
        listViewContato.setAdapter(adapterContato);
        listViewContato.setOnItemClickListener(onItemClickListener);
    }

    private List<Contato> getContatos() {
        List<Contato> contatos = new ArrayList<Contato>();

        try {
            Cursor cursorContato = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            while (cursorContato.moveToNext()) {
                int indexContatoId = cursorContato.getColumnIndex(ContactsContract.Contacts._ID);
                String contatoId = cursorContato.getString(indexContatoId);

                int indexDisplayName = cursorContato.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String nome = cursorContato.getString(indexDisplayName);

                int indexHasPhone = cursorContato.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                int hasPhone = cursorContato.getInt(indexHasPhone);

                if (hasPhone == 1) {
                    Cursor phoneCursor = getActivity().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "='" + contatoId + "'", null, null);

                    while (phoneCursor.moveToNext()) {
                        int indexNumber = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String numero = phoneCursor.getString(indexNumber);

                        Contato oContact = new Contato();

                        oContact.setContatoId(contatoId);
                        oContact.setNome(nome);
                        oContact.setNumero(numero);

                        contatos.add(oContact);
                    }
                    phoneCursor.close();
                } else {
                    Contato oContact = new Contato();
                    oContact.setContatoId(contatoId);
                    oContact.setNome(nome);
                    contatos.add(oContact);
                }
            }
            cursorContato.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contatos;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (OnListaSelecionada) context;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Contato contato = (Contato) parent.getItemAtPosition(position);
            mCallback.onListaSelecionada(contato);
        }
    };
}
