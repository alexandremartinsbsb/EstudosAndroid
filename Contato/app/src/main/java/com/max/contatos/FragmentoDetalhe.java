package com.max.contatos;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentoDetalhe extends Fragment {

    private Contato contato;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(Boolean.TRUE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_detalhe, container, Boolean.FALSE);

        Button btnCobrar = (Button) view.findViewById(R.id.btnChamarCobrar);
        Button btnNormal = (Button) view.findViewById(R.id.btnChamarNormal);

        btnCobrar.setOnClickListener(onClickListener);
        btnNormal.setOnClickListener(onClickListener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragmento_detalhe, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar:
                String contatoId = contato.getContatoId();
                String uri = ContactsContract.Contacts.CONTENT_URI + "/" + contatoId;
                Intent intent = new Intent(Intent.ACTION_EDIT, Uri.parse(uri));
                intent.putExtra("finishActivityOnSaveCompleted", Boolean.TRUE);
                startActivityForResult(intent, 1);

                return Boolean.TRUE;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            atualizarContato();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void atualizarContato() {
        try {
            Cursor phoneCursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "='" + this.contato.getContatoId() + "'", null, null);
            if (phoneCursor.moveToNext()) {
                int indexNumero = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String numero = phoneCursor.getString(indexNumero);
                this.contato.setNumero(numero);

                TextView textViewNumero = (TextView) getView().findViewById(R.id.textViewTelefone);
                textViewNumero.setText("Número: " + contato.getNumero());
            }
            phoneCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String num;

            if (v.getId() == R.id.btnChamarCobrar) {
                num = contato.getNumeroCobrar();
            } else {
                num = contato.getNumero();
            }

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
            startActivity(intent);
        }
    };

    public void lerContato(Contato contato) {
        if (this.contato == null)
            this.contato = contato;

        TextView textViewNome = (TextView) getView().findViewById(R.id.textViewNome);
        TextView textViewNumero = (TextView) getView().findViewById(R.id.textViewTelefone);

        textViewNome.setText("Nome: " + contato.getNome());
        textViewNumero.setText("Número: " + contato.getNumero());
    }
}
