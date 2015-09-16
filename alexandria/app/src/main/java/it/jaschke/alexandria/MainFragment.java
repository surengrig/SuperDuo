package it.jaschke.alexandria;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import icepick.Icepick;
import icepick.State;
import it.jaschke.alexandria.api.BookListAdapter;
import it.jaschke.alexandria.data.AlexandriaContract;

/**
 * Created by Suren Grigoryan on 9/14/15.
 */
public class MainFragment extends Fragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private BookListAdapter bookListAdapter;
    private ListView bookList;
    @State
    String mSearchText;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        bookListAdapter = new BookListAdapter(getActivity(), null, 0);
        bookListAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                final String selection = AlexandriaContract.BookEntry.TITLE + " LIKE ? OR "
                        + AlexandriaContract.BookEntry.SUBTITLE + " LIKE ? ";
                String searchString = constraint.toString();

                searchString = "%" + searchString + "%";
                return getActivity().getContentResolver().query(
                        AlexandriaContract.BookEntry.CONTENT_URI,
                        null,
                        selection,
                        new String[]{searchString, searchString},
                        null
                );
            }
        });

        bookList = (ListView) rootView.findViewById(R.id.listOfBooks);
        bookList.setAdapter(bookListAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = bookListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    String ean = cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID));
                    Bundle args = new Bundle();
                    args.putString(BookDetailsActivity.EAN_KEY, ean);

                    Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
                    intent.putExtra(BookDetailsActivity.EAN_KEY, ean);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main2, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                searchView.setOnQueryTextListener(this);
                if(mSearchText !=null){
                    searchView.setIconified(false);
                    searchItem.expandActionView();
                    searchView.setQuery(mSearchText, false);
                    searchView.clearFocus();
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        bookListAdapter.getFilter().filter(query);
        mSearchText = query;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        bookListAdapter.getFilter().filter(newText);
        mSearchText = newText;
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                AlexandriaContract.BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookListAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle out){
        Icepick.saveInstanceState(this, out);
        super.onSaveInstanceState(out);
    }
}




