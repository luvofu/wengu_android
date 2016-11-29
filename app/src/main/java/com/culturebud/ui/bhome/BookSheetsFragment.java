package com.culturebud.ui.bhome;

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
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/17.
 */

@PresenterInject(MyFavoritesPresenter.class)
public class BookSheetsFragment extends BaseFragment<MyFavoritesContract.Presenter> implements MyFavoritesContract.View {
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
        rvBookSheets.setAdapter(new BookSheetsAdapter());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getMyFavoriteBookSheets(0);
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
}
