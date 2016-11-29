package com.culturebud.ui.bhome;

import android.os.Bundle;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.NoteContract;
import com.culturebud.presenter.NotePresenter;

/**
 * Created by XieWei on 2016/11/25.
 */

@PresenterInject(NotePresenter.class)
public class EditNoteActivity extends BaseActivity<NoteContract.Presenter> implements NoteContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        presenter.setView(this);
        showTitlebar();
        setTitle("编辑笔记");
    }

    @Override
    public void onNoteOpera(boolean res, int operaType) {
        if (res && NoteContract.View.OPERA_TYPE_EDIT == operaType) {

        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}
