package com.culturebud;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class TextEditorFragment extends BaseFragment implements View.OnClickListener  {

    private class TextEditorParameter {
        private int requestCode;
        private String content;
        private String title;
        private int type = 0;
        private String hint;
        private int contentLength = 95;

        private Boolean needShowTip = false; //底部是否显示提示.
        private String tips;  //底部提示内容 （某人是隐藏的，不显示）

        public TextEditorParameter(Bundle bundle){
            this.requestCode = bundle.getInt("requestCode");
            this.title = bundle.getString("title");
            this.content = bundle.getString("content");
            this.hint = bundle.getString("hint");
            this.contentLength = bundle.getInt("contentLength");
            this.type = bundle.getInt("type");
            this.contentLength = bundle.getInt("contentLength");
            this.needShowTip = bundle.getBoolean("needShowTip");
            this.tips = bundle.getString("tips");
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public int getContentLength() {
            return contentLength;
        }

        public void setContentLength(int contentLength) {
            this.contentLength = contentLength;
        }

        public Boolean getNeedShowTip() {
            return needShowTip;
        }

        public void setNeedShowTip(Boolean needShowTip) {
            this.needShowTip = needShowTip;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }

    private EditText etInput;
    private TextView placeholderView;
    private TextEditorParameter editorParameter;

    public void setEditorParameter(TextEditorParameter editorParameter) {
        this.editorParameter = editorParameter;
    }

    //初始化自己.
    public static TextEditorFragment newInstance(int requestCode,String title, String content, String hint, int contentLength, int type, Boolean needshowtip, String tips){
        TextEditorFragment myFragment = new TextEditorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("hint", hint);
        bundle.putInt("contentLength", contentLength);
        bundle.putInt("type", type);
        bundle.putBoolean("needShowTip", needshowtip);
        bundle.putString("tips", tips);
        bundle.putInt("requestCode", requestCode);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    //获取tag
    public static String getFragmentTag () {
        return "textEditorFragment";
    }

    //是否正在显示.
    public static Boolean isShowing (Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag());
        return fragment != null;
    }

    public TextEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            TextEditorParameter editorParameter = new TextEditorParameter(bundle);
            setEditorParameter(editorParameter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.fragment_text_editor);
        showTitle(editorParameter.getTitle());
        initTitleLeft(R.layout.titlebar_left_back);
        initTitleRight(R.layout.titlebar_right_text);

        etInput = (EditText) view.findViewById(R.id.et_alter_user_info);
        placeholderView = (TextView) view.findViewById(R.id.et_alter_wy_account);

        initData();

        setListeners(view);

        return view;
    }

    public void setListeners(View view) {
        View backView = view.findViewById(R.id.iv_back);
        backView.setOnClickListener(this);

        View confirmView = view.findViewById(R.id.iv_confirm);
        confirmView.setOnClickListener(this);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:{
                //点击返回.
                getActivity().onBackPressed();
            }
                break;
            case R.id.iv_confirm: {
                //点击确定.
                if (mListener != null) {
                    String editString = etInput.getText().toString();

                    //隐藏键盘.
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
                    }

                    //判断编辑后跟编辑前内容是否一样，若一样，则直接退出自己.
                    String oldString = editorParameter.getContent();
                    if (!TextUtils.isEmpty(oldString) && editString.equals(oldString)) {
                        mListener.onExist();
                    } else  {
                        mListener.onConfirmSubmission(editString, editorParameter.getRequestCode());
                    }
                }
            }
            break;
        }
    }

    private void initData() {
        switch (editorParameter.getType()) {
            case 0://昵称
                break;
            case 1://邮箱
                etInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 2://签名
                etInput.setMinLines(6);
                etInput.setGravity(Gravity.TOP | Gravity.LEFT);
                etInput.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etInput.setSingleLine(false);
                break;
        }
        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editorParameter.getContentLength())});
        if (!TextUtils.isEmpty(editorParameter.getHint())) {
            etInput.setHint(editorParameter.getHint());
        }
        if (editorParameter.getContent() != null) {
            etInput.setText(editorParameter.getContent());
            etInput.setSelection(editorParameter.getContent().length());
        }

        if (editorParameter.getNeedShowTip()) {
            //如果需要显示，将placeholderView控件显示，并赋值.
            if (!TextUtils.isEmpty(editorParameter.getTips())) {
                placeholderView.setVisibility(View.VISIBLE);
                placeholderView.setText(editorParameter.getTips());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && activity instanceof OnFragmentInteractionListener) {
            mListener =  (OnFragmentInteractionListener)activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private OnFragmentInteractionListener mListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onConfirmSubmission(String inputString, int requestCode);

        void onExist();
    }
}

