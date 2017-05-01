package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.bigkoo.pickerview.OptionsPickerView;
import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst.ContentPermission;
import com.culturebud.R;
import com.culturebud.adapter.NotebookDetailAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Note;
import com.culturebud.bean.NotebookDetail;
import com.culturebud.bean.User;
import com.culturebud.contract.NotebookDetailContract;
import com.culturebud.presenter.NotebookDetailPresenter;
import com.culturebud.ui.image.PreviewBigImgActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_CREATE_NOTE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTEBOOK_NAME;

/**
 * Created by XieWei on 2016/11/22.
 */

@PresenterInject(NotebookDetailPresenter.class)
public class NotebookDetailActivity extends BaseActivity<NotebookDetailContract.Presenter>
        implements NotebookDetailContract.View, NotebookDetailAdapter.OnNotebookOperaListener,
        OptionsPickerView.OnOptionsSelectListener {
    private static final String TAG = NotebookDetailActivity.class.getSimpleName();
    private RecyclerView rvNotes;
    private OptionsPickerView<String> permissionOpts;
    private NotebookDetail notebookDetail;
    private int currentPage;
    private PopupWindow pwItemMenu;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        rvNotes = new RecyclerView(this);
        rvNotes.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNotes.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        divider.setDividerHeight(10);
        rvNotes.addItemDecoration(divider);
        setContentView(rvNotes);
        NotebookDetailAdapter adapter = new NotebookDetailAdapter();
        adapter.setOnNotebookOperaListener(this);
        rvNotes.setAdapter(adapter);
        showTitlebar();
        setTitle(R.string.detail);
        long notebookId = getIntent().getLongExtra("notebookId", -1);
        userId = getIntent().getLongExtra("user_id", -1);
        if (notebookId == -1) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        if (user != null && user.getUserId() == userId) {
            showOperas();
            setOperasDrawable(R.drawable.titlebar_add_selector);
            presenter.notebookDetail(notebookId);
        } else {
            hideOpears();
            adapter.setHasHeader(false);
            presenter.notesForNotebook(notebookId, currentPage);
        }
    }

    private void initItemMenu() {
        if (pwItemMenu == null) {
            pwItemMenu = new PopupWindow(this, null, R.style.PopupWindow);
            View view = getLayoutInflater().inflate(R.layout.popup_menu_bg_top, null);
            view.findViewById(R.id.tv_edit).setOnClickListener(this);
            view.findViewById(R.id.tv_delete).setOnClickListener(this);
            pwItemMenu.setContentView(view);
            pwItemMenu.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            pwItemMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pwItemMenu.setOutsideTouchable(true);
            pwItemMenu.setFocusable(true);
            pwItemMenu.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void initPermissionDialog(int permission) {
        if (permissionOpts == null) {
            permissionOpts = new OptionsPickerView<>(this);
            ArrayList<String> items = new ArrayList<>();
            items.add(ContentPermission.PER_DES_PUBLIC);
            items.add(ContentPermission.PER_DES_FRIEND);
            items.add(ContentPermission.PER_DES_PERSONAL);
            permissionOpts.setPicker(items);
            permissionOpts.setSelectOptions(permission);
            permissionOpts.setCyclic(false);
            permissionOpts.setOnoptionsSelectListener(this);
        } else {
            permissionOpts.setSelectOptions(permission);
        }
    }

    @Override
    public void onNotebookDetail(NotebookDetail notebookDetail) {
        this.notebookDetail = notebookDetail;
        ((NotebookDetailAdapter) rvNotes.getAdapter()).setNotebookDetail(notebookDetail);
        presenter.notesForNotebook(notebookDetail.getNotebookId(), currentPage);
    }

    @Override
    public void onRelationType(int relationType) {
    }

    @Override
    public void onNotes(List<Note> notes) {
        if (currentPage == 0) {
            ((NotebookDetailAdapter) rvNotes.getAdapter()).clearData();
        }
        ((NotebookDetailAdapter) rvNotes.getAdapter()).addItems(notes);
    }

    @Override
    public void onNotebookPermissionEdit(boolean res, long notebookId, int permission) {

    }

    @Override
    public void onNotebookEdit(boolean res, long notebookId, String name) {

    }

    @Override
    public void onDeleteNote(boolean res, long noteId, int position) {
        if (res) {
            ((NotebookDetailAdapter) rvNotes.getAdapter()).deleteItem(noteId, position);
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_operas: {
                if (notebookDetail == null) {
                    return;
                }
                Intent intent = new Intent(this, CreateNoteActivity.class);
                intent.putExtra("notebookId", notebookDetail.getNotebookId());
                intent.putExtra("notebookTitle", notebookDetail.getTitle());
                startActivityForResult(intent, REQUEST_CODE_CREATE_NOTE);
                break;
            }
            case R.id.tv_edit: {
                pwItemMenu.dismiss();
                if (currentOperaNote == null) {
                    return;
                }
                Intent intent = new Intent(this, EditNoteActivity.class);
                intent.putExtra("note", new Gson().toJson(currentOperaNote));
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
                break;
            }
            case R.id.tv_delete:
                pwItemMenu.dismiss();
                presenter.deleteNote(currentOperaNote.getNoteId(), currentOperaPosition);
                break;
        }
    }

    @Override
    public void onNotebookOpera(View v, NotebookDetail notebookDetail, int operaType, int position) {
        switch (operaType) {
            case NotebookDetailAdapter.OPERA_TYPE_NOTEBOOK_NAME:
                Intent intent = new Intent(this, EditNotebookNameActivity.class);
                intent.putExtra("notebookId", notebookDetail.getNotebookId());
                intent.putExtra("notebookName", notebookDetail.getName());
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTEBOOK_NAME);
                break;
            case NotebookDetailAdapter.OPERA_TYPE_PERMISSION:
                initPermissionDialog(notebookDetail.getPermission());
                permissionOpts.show();
                break;
        }
    }

    private Note currentOperaNote;
    private int currentOperaPosition;

    @Override
    public void onNoteOpera(View v, Note note, int operaType, int position) {
        switch (operaType) {
            case NotebookDetailAdapter.OPERA_TYPE_NOTE_EDIT_DELETE:
                currentOperaNote = note;
                currentOperaPosition = position;
                initItemMenu();
                if (pwItemMenu.isShowing()) {
                    pwItemMenu.dismiss();
                }
                int[] locs = new int[2];
                v.getLocationOnScreen(locs);
                int y = locs[1] - getResources().getDimensionPixelSize(R.dimen.item_popup_menu_height) + v.getHeight
                        () / 2;
                pwItemMenu.showAtLocation(v, Gravity.NO_GRAVITY, locs[0], y);
                break;
            case NotebookDetailAdapter.OPERA_TYPE_NOTE_PREVIEW_PIC: {
                Intent intent = new Intent(this, PreviewBigImgActivity.class);
                intent.putExtra("img_url", note.getImage());
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3) {
        Log.e(TAG, "options1 = " + options1 + ", " + "options2 = " + options2 + ", " + "options3 = " + options3);
        if (notebookDetail != null) {
            notebookDetail.setPermission(options1);
            rvNotes.getAdapter().notifyItemChanged(0);
            presenter.notebookPermissionEdit(notebookDetail.getNotebookId(), options1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDIT_NOTEBOOK_NAME:
                if (resultCode == RESULT_OK) {
                    long notebookId = data.getLongExtra("notebookId", -1);
                    String nbName = data.getStringExtra("notebookName");
                    if (notebookId == notebookDetail.getNotebookId()) {
                        presenter.notebookEdit(notebookId, nbName);
                        notebookDetail.setName(nbName);
                        rvNotes.getAdapter().notifyItemChanged(0);
                    }
                }
                break;
            case REQUEST_CODE_CREATE_NOTE:
                if (resultCode == RESULT_OK) {
                    presenter.notesForNotebook(notebookDetail.getNotebookId(), currentPage);
                }
                break;
        }
    }
}
