package com.max.contatos;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class DetalheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);

        if (getIntent().hasExtra("contatoId")) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentoDetalhe fragmentoDetalhe = new FragmentoDetalhe();
            fragmentTransaction.add(android.R.id.content, fragmentoDetalhe, "fragmentoDetalhe");
            fragmentTransaction.commit();
        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String contatoId = getIntent().getExtras().getString("contatoId");
        String nome = getIntent().getExtras().getString("nome");
        String numero = getIntent().getExtras().getString("numero");

        Contato contato = new Contato();
        contato.setNome(nome);
        contato.setNumero(numero);
        contato.setContatoId(contatoId);

        FragmentoDetalhe fragmentoDetalhe = (FragmentoDetalhe) getFragmentManager().findFragmentByTag("fragmentoDetalhe");

        if (fragmentoDetalhe != null) {
            fragmentoDetalhe.lerContato(contato);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return Boolean.TRUE;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
