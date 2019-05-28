package com.example.agenda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AgendaAdapter extends ArrayAdapter<Agenda> {
    Context context;
    int layoutResourceId;
    List<Agenda> dados;

    public AgendaAdapter(Context context, int layoutResourceId, List<Agenda> dados){
        super(context, layoutResourceId,  dados);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.dados = dados;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(layoutResourceId, parent, false);
        }

        TextView nome = (TextView) view.findViewById(R.id.txtNomeItem);
        TextView telefone = (TextView) view.findViewById(R.id.txtTelefoneItem);
        ImageView foto = (ImageView) view.findViewById(R.id.imgContatoItem);

        Agenda contato = dados.get(position);

        nome.setText(contato.getNome());
        telefone.setText(contato.getTelefone());

        byte[] img = contato.getImagem();
        if (img != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            foto.setImageBitmap(bitmap);
        }

        return view;
    }

    public void setDados(List<Agenda> dados){
        this.clear();
        this.addAll(dados);
        this.dados = dados;
        this.notifyDataSetChanged();
    }

    // por padrão este método retorna o índice da lista, e no nosso caso queremos que ele retonrne o id do contato que está no banco de dados
    @Override
    public long getItemId(int position) {
        return dados.get(position).getId();
    }
}
