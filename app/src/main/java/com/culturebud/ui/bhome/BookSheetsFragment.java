package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.adapter.BookSheetsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.contract.MyFavoritesContract;
import com.culturebud.presenter.MyFavoritesPresenter;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/17.
 */

@PresenterInject(MyFavoritesPresenter.class)
public class BookSheetsFragment extends BaseFragment<MyFavoritesContract.Presenter> implements MyFavoritesContract.View,
        BookSheetsAdapter.OnItemClickListener, BookSheetsAdapter.OnItemDeleteListener {
    private static final String TAG = BookSheetsFragment.class.getSimpleName();
    private RecyclerView rvBookSheets;
    private int currentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_sheets, container, false);
        rvBookSheets = (RecyclerView) view.findViewById(R.id.rv_book_sheets);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBookSheets.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
        rvBookSheets.addItemDecoration(divider);
        BookSheetsAdapter adapter = new BookSheetsAdapter();
        adapter.setOnItemClickListener(this);
        adapter.setDeleteListener(this);
        rvBookSheets.setAdapter(adapter);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            presenter.getMyFavoriteBookSheets(0);
        }
    }

    @Override
    public void onBooks(List<Book> books) {

    }

    @Override
    public void onBookSheets(List<BookSheet> bookSheets) {
        if (currentPage == 0) {
            ((BookSheetsAdapter) rvBookSheets.getAdapter()).clearData();
        }
        ((BookSheetsAdapter) rvBookSheets.getAdapter()).addItems(bookSheets);
    }

    @Override
    public void onDelMyFavorite(int type, long id, boolean success) {
        if (success) {
            ((BookSheetsAdapter) rvBookSheets.getAdapter()).deleteItem(id);
        }
    }

    @Override
    public void onItemClick(View v, int position, BookSheet bookSheet) {
        Intent intent = new Intent(getActivity(), BookSheetDetailActivity.class);
        intent.putExtra("sheetId", bookSheet.getSheetId());
        startActivity(intent);
    }

    @Override
    public void onItemDelete(int position, BookSheet bookSheet) {
        presenter.deleteMyFavorite(1, bookSheet.getSheetId());
    }
}
