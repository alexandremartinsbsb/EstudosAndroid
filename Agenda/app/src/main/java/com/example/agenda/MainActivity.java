package com.example.agenda;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LOADER_ID = 1;
    private AgendaAdapter agendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        agendaAdapter = new AgendaAdapter(getApplicationContext(), R.layout.contato_item, new ArrayList<Agenda>());

        ListView listView = (ListView) findViewById(R.id.listViewContatos);
        listView.setAdapter(agendaAdapter);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, (android.app.LoaderManager.LoaderCallbacks<Object>) cursorLoaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getBaseContext(), ContatoInserirActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            if (bundle != null) {
                if (bundle.containsKey("filter")) {
                    String filter = bundle.getString("filter");

                    String selection = AgendaDbHelper.C_NOME + " like ? or " + AgendaDbHelper.C_TELEFONE + " like ? ";
                    String[] selectionBundle = {" %" + filter + "% ", " %" + filter + "% "};
                    return new CursorLoader(getApplicationContext(), AgendaProvider.CONTENT_URI, null, selection, selectionBundle, null);
                }
            }

            return new CursorLoader(getApplicationContext(), AgendaProvider.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            List<Agenda> agendaList = getList(cursor);
            agendaAdapter.setDados(agendaList);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            agendaAdapter.setDados(new ArrayList<Agenda>());
        }
    };

    private List<Agenda> getList(Cursor cursor) {
        List<Agenda> agendaList = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Agenda contato = new Agenda();
                    contato.setId(cursor.getLong(0));
                    contato.setNome(cursor.getString(1));
                    contato.setTelefone(cursor.getString(2));
                    contato.setImagem(cursor.getBlob(3));

                    agendaList.add(contato);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agendaList;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            Bundle param = new Bundle();
            param.putString("filter", s);

            android.app.LoaderManager lm = getLoaderManager();
            lm.initLoader(LOADER_ID, param, (android.app.LoaderManager.LoaderCallbacks<Object>) cursorLoaderCallbacks);

            return true;
        }
    };
}
