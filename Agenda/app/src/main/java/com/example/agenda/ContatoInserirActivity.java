package com.example.agenda;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ContatoInserirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_inserir);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(Boolean.TRUE);

        findViewById(R.id.buttonImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione a foto"), 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_inserir_contato, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_OK && requestCode == 1) {
            Uri imagemSelecionada = data.getData();
            ImageView foto = (ImageView) findViewById(R.id.imageViewFoto);
            foto.setImageURI(imagemSelecionada);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                salvar();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void salvar() {
        EditText txtNome = (EditText) findViewById(R.id.editTextNome);
        EditText txtTelefone = (EditText) findViewById(R.id.editTextTelefone);
        ImageView foto = (ImageView) findViewById(R.id.imageViewFoto);

        String nome = txtNome.getText().toString();
        String telefone = txtTelefone.getText().toString();

        Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] img = outputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(AgendaDbHelper.C_NOME, nome);
        values.put(AgendaDbHelper.C_TELEFONE, telefone);
        values.put(AgendaDbHelper.C_FOTO, img);

        getContentResolver().insert(AgendaProvider.CONTENT_URI, values);

        startActivity(new Intent(this, MainActivity.class));
    }
}
