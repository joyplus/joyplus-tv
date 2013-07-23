package com.joyplus.tv.ui;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.joyplus.tv.R;

public class KeyBoardView extends LinearLayout implements android.view.View.OnClickListener, android.view.View.OnKeyListener{
	
	private static final String TAG = "KeyBoardView";
	
	private EditText mEditText;
	private OnKeyBoardResultListener listener;

	public KeyBoardView(Context context, EditText editView, OnKeyBoardResultListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mEditText = editView;
		this.listener = listener;
		View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keybard, null);
		
		Button btn_0 = (Button) rootView.findViewById(R.id.key_0);
		Button btn_1 = (Button) rootView.findViewById(R.id.key_1);
		Button btn_2 = (Button) rootView.findViewById(R.id.key_2);
		Button btn_3 = (Button) rootView.findViewById(R.id.key_3);
		Button btn_4 = (Button) rootView.findViewById(R.id.key_4);
		Button btn_5 = (Button) rootView.findViewById(R.id.key_5);
		Button btn_6 = (Button) rootView.findViewById(R.id.key_6);
		Button btn_7 = (Button) rootView.findViewById(R.id.key_7);
		Button btn_8 = (Button) rootView.findViewById(R.id.key_8);
		Button btn_9 = (Button) rootView.findViewById(R.id.key_9);
		
		Button btn_a = (Button) rootView.findViewById(R.id.key_a);
		Button btn_b = (Button) rootView.findViewById(R.id.key_b);
		Button btn_c = (Button) rootView.findViewById(R.id.key_c);
		Button btn_d = (Button) rootView.findViewById(R.id.key_d);
		Button btn_e = (Button) rootView.findViewById(R.id.key_e);
		Button btn_f = (Button) rootView.findViewById(R.id.key_f);
		Button btn_g = (Button) rootView.findViewById(R.id.key_g);
		Button btn_h = (Button) rootView.findViewById(R.id.key_h);
		Button btn_i = (Button) rootView.findViewById(R.id.key_i);
		Button btn_j = (Button) rootView.findViewById(R.id.key_j);
		Button btn_k = (Button) rootView.findViewById(R.id.key_k);
		Button btn_l = (Button) rootView.findViewById(R.id.key_l);
		Button btn_m = (Button) rootView.findViewById(R.id.key_m);
		Button btn_n = (Button) rootView.findViewById(R.id.key_n);
		Button btn_o = (Button) rootView.findViewById(R.id.key_o);
		Button btn_p = (Button) rootView.findViewById(R.id.key_p);
		Button btn_q = (Button) rootView.findViewById(R.id.key_q);
		Button btn_r = (Button) rootView.findViewById(R.id.key_r);
		Button btn_s = (Button) rootView.findViewById(R.id.key_s);
		Button btn_t = (Button) rootView.findViewById(R.id.key_t);
		Button btn_u = (Button) rootView.findViewById(R.id.key_u);
		Button btn_v = (Button) rootView.findViewById(R.id.key_v);
		Button btn_w = (Button) rootView.findViewById(R.id.key_w);
		Button btn_x = (Button) rootView.findViewById(R.id.key_x);
		Button btn_y = (Button) rootView.findViewById(R.id.key_y);
		Button btn_z = (Button) rootView.findViewById(R.id.key_z);

//		Button btn_doute = (Button) rootView.findViewById(R.id.key_doute);
		Button btn_backspace = (Button) rootView.findViewById(R.id.key_backspace);
		Button btn_canle = (Button) rootView.findViewById(R.id.key_cancle);
		Button btn_search = (Button) rootView.findViewById(R.id.key_search);
		
		btn_0.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);
		
		btn_a.setOnClickListener(this);
		btn_b.setOnClickListener(this);
		btn_c.setOnClickListener(this);
		btn_d.setOnClickListener(this);
		btn_e.setOnClickListener(this);
		btn_f.setOnClickListener(this);
		btn_g.setOnClickListener(this);
		btn_h.setOnClickListener(this);
		btn_i.setOnClickListener(this);
		btn_j.setOnClickListener(this);
		btn_k.setOnClickListener(this);
		btn_l.setOnClickListener(this);
		btn_m.setOnClickListener(this);
		btn_n.setOnClickListener(this);
		btn_o.setOnClickListener(this);
		btn_p.setOnClickListener(this);
		btn_q.setOnClickListener(this);
		btn_r.setOnClickListener(this);
		btn_s.setOnClickListener(this);
		btn_t.setOnClickListener(this);
		btn_u.setOnClickListener(this);
		btn_v.setOnClickListener(this);
		btn_w.setOnClickListener(this);
		btn_x.setOnClickListener(this);
		btn_y.setOnClickListener(this);
		btn_z.setOnClickListener(this);
		
		btn_0.setOnKeyListener(this);
		btn_1.setOnKeyListener(this);
		btn_2.setOnKeyListener(this);
		btn_3.setOnKeyListener(this);
		btn_4.setOnKeyListener(this);
		btn_5.setOnKeyListener(this);
		btn_6.setOnKeyListener(this);
		btn_7.setOnKeyListener(this);
		btn_8.setOnKeyListener(this);
		btn_9.setOnKeyListener(this);
		
		btn_a.setOnKeyListener(this);
		btn_b.setOnKeyListener(this);
		btn_c.setOnKeyListener(this);
		btn_d.setOnKeyListener(this);
		btn_e.setOnKeyListener(this);
		btn_f.setOnKeyListener(this);
		btn_g.setOnKeyListener(this);
		btn_h.setOnKeyListener(this);
		btn_i.setOnKeyListener(this);
		btn_g.setOnKeyListener(this);
		btn_k.setOnKeyListener(this);
		btn_l.setOnKeyListener(this);
		btn_m.setOnKeyListener(this);
		btn_n.setOnKeyListener(this);
		btn_o.setOnKeyListener(this);
		btn_p.setOnKeyListener(this);
		btn_q.setOnKeyListener(this);
		btn_r.setOnKeyListener(this);
		btn_s.setOnKeyListener(this);
		btn_t.setOnKeyListener(this);
		btn_u.setOnKeyListener(this);
		btn_v.setOnKeyListener(this);
		btn_w.setOnKeyListener(this);
		btn_x.setOnKeyListener(this);
		btn_y.setOnKeyListener(this);
		btn_z.setOnKeyListener(this);
		
		btn_canle.setOnKeyListener(this);
		btn_backspace.setOnKeyListener(this);
		btn_search.setOnKeyListener(this);
		
//		btn_doute.setOnClickListener(this);
		btn_backspace.setOnClickListener(this);
		btn_canle.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		
		btn_a.setNextFocusLeftId(R.id.key_3);
		btn_h.setNextFocusLeftId(R.id.key_6);
		btn_o.setNextFocusLeftId(R.id.key_9);
		btn_u.setNextFocusLeftId(R.id.key_cancle);
		
		rootView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		addView(rootView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Editable editable = mEditText.getText();
		int start = mEditText.getSelectionStart();
		switch (v.getId()) {
		case R.id.key_backspace:
			if (editable != null && editable.length() > 0) {
				if (start > 0) {
					editable.delete(start - 1, start);
				}
			}
			break;
		case R.id.key_search:
			if(listener!=null){
				listener.onResult(true);
			}
			break;
		case R.id.key_cancle:
			if(listener!=null){
				listener.onResult(false);
			}
			break;
		default:
			editable.insert(start, ((Button)v).getText());
			break;
		}
	}
	
	public interface OnKeyBoardResultListener{
		abstract void onResult(boolean isSearch);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_UP){
			if((keyCode<=KeyEvent.KEYCODE_9&&keyCode>=KeyEvent.KEYCODE_0)||(keyCode<=KeyEvent.KEYCODE_Z&&keyCode>=KeyEvent.KEYCODE_A)){
				Editable editable = mEditText.getText();
				int start = mEditText.getSelectionStart();
				String string = null;
				switch (keyCode) {
				case KeyEvent.KEYCODE_0:
					string = "0";
					break;
				case KeyEvent.KEYCODE_1:
					string = "1";
					break;
				case KeyEvent.KEYCODE_2:
					string = "2";
					break;
				case KeyEvent.KEYCODE_3:
					string = "3";
					break;
				case KeyEvent.KEYCODE_4:
					string = "4";
					break;
				case KeyEvent.KEYCODE_5:
					string = "5";
					break;
				case KeyEvent.KEYCODE_6:
					string = "6";
					break;
				case KeyEvent.KEYCODE_7:
					string = "7";
					break;
				case KeyEvent.KEYCODE_8:
					string = "8";
					break;
				case KeyEvent.KEYCODE_9:
					string = "9";
					break;
				case KeyEvent.KEYCODE_A:
					string = "A";
					break;
				case KeyEvent.KEYCODE_B:
					string = "B";
					break;
				case KeyEvent.KEYCODE_C:
					string = "C";
					break;
				case KeyEvent.KEYCODE_D:
					string = "D";
					break;
				case KeyEvent.KEYCODE_E:
					string = "E";
					break;
				case KeyEvent.KEYCODE_F:
					string = "F";
					break;
				case KeyEvent.KEYCODE_H:
					string = "H";
					break;
				case KeyEvent.KEYCODE_I:
					string = "I";
					break;
				case KeyEvent.KEYCODE_J:
					string = "J";
					break;
				case KeyEvent.KEYCODE_K:
					string = "K";
					break;
				case KeyEvent.KEYCODE_L:
					string = "L";
					break;
				case KeyEvent.KEYCODE_M:
					string = "M";
					break;
				case KeyEvent.KEYCODE_N:
					string = "N";
					break;
				case KeyEvent.KEYCODE_O:
					string = "O";
					break;
				case KeyEvent.KEYCODE_P:
					string = "P";
					break;
				case KeyEvent.KEYCODE_Q:
					string = "Q";
					break;
				case KeyEvent.KEYCODE_R:
					string = "R";
					break;
				case KeyEvent.KEYCODE_S:
					string = "S";
					break;
				case KeyEvent.KEYCODE_T:
					string = "T";
					break;
				case KeyEvent.KEYCODE_U:
					string = "U";
					break;
				case KeyEvent.KEYCODE_V:
					string = "V";
					break;
				case KeyEvent.KEYCODE_W:
					string = "W";
					break;
				case KeyEvent.KEYCODE_X:
					string = "X";
					break;
				case KeyEvent.KEYCODE_Y:
					string = "Y";
					break;
				case KeyEvent.KEYCODE_Z:
					string = "Z";
					break;
				}
				if(string!=null){
					editable.insert(start, string);
				}
			}else if(keyCode == KeyEvent.KEYCODE_BACK){
				if(listener!=null){
					listener.onResult(false);
				}
			}else if(keyCode == KeyEvent.KEYCODE_DEL){
				Editable editable = mEditText.getText();
				int start = mEditText.getSelectionStart();
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "dispatchKeyEvent--->" + event.getKeyCode());
		
		return super.dispatchKeyEvent(event);
	}

}
