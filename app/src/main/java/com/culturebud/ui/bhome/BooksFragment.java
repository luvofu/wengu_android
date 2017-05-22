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
import com.culturebud.adapter.BooksAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.contract.MyFavoritesContract;
import com.culturebud.presenter.CollectedBooksPresenter;
import com.culturebud.presenter.MyFavoritesPresenter;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.j256.ormlite.stmt.query.In;

import java.util.List;

/**
 * Created by XieWei on 2016/11/17.
 */

@PresenterInject(MyFavoritesPresenter.class)
public class BooksFragment extends BaseFragment<MyFavoritesContract.Presenter> implements MyFavoritesContract.View,
        BooksAdapter.OnItemClickListener {
    private static final String TAG = BooksFragment.class.getSimpleName();
    private RecyclerView rvBooks;
    private int currentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.books, container, false);
        rvBooks = (RecyclerView) view.findViewById(R.id.content_view);

        setNoDataView(R.id.main_multiplestatusview, view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
        rvBooks.addItemDecoration(divider);
        BooksAdapter adapter = new BooksAdapter(0);
        rvBooks.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.getMyFavoriteBooks(0);
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            presenter.getMyFavoriteBooks(currentPage);
//        }
//    }

    @Override
    public void onBooks(List<Book> books) {
        if (currentPage == 0) {
            ((BooksAdapter) rvBooks.getAdapter()).clearData();
        }
        ((BooksAdapter) rvBooks.getAdapter()).addItems(books);
    }

    @Override
    public void onBookSheets(List<BookSheet> bookSheets) {

    }

    @Override
    public void onDelMyFavorite(int type, long id, boolean success) {
        if (success) {
            ((BooksAdapter) rvBooks.getAdapter()).deleteItem(id);
        }
    }

    @Override
    public void onItemClick(View v, Book book, int operaType) {
        if (operaType == 0) {
            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
            intent.putExtra("bookId", book.getBookId());
            startActivity(intent);
        } else if (operaType == 1) {
            presenter.deleteMyFavorite(0, book.getBookId());
        }
    }

    @Override
    public void onRetryData() {
        presenter.getMyFavoriteBooks(0);
    }
}
