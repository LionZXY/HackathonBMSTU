package ru.skafcats.hackathon2017;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;

import biz.borealis.numberpicker.NumberPicker;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.helpers.TaskHelper;
import ru.skafcats.crypto.interfaces.ITaskAnswerListener;
import ru.skafcats.crypto.models.SecureInfo;
import ru.skafcats.hackathon2017.adapters.AttachmentAdapter;
import ru.skafcats.hackathon2017.tasks.RemoveBufferTask;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class SecureInfoActivity extends Activity {
    public static final int REQUEST_PERMISSION = 20;
    public static final int FILE_SELECT = 21;
    private SecureInfo info;
    private RecyclerView recyclerView = null;
    private AttachmentAdapter adapter = null;

    private TextView name = null;
    private TextView symbol = null;
    private EditText login = null;
    private TextView password = null;
    private ImageView image = null;

    private final View.OnClickListener onPasswordClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setItems(R.array.password_choise, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ClipboardManager clipboardManager;
                    ClipData clipData;
                    switch (which) {
                        case 0:
                            final EditText input = new EditText(v.getContext());
                            input.setMaxLines(1);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(v.getContext()).setTitle("Введите пароль").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    info.addField("password", input.getText().toString());
                                    password.setText("****");
                                    dialog.dismiss();
                                }
                            });
                            builder2.setView(input).create().show();
                            break;
                        case 1:
                            clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            clipData = ClipData.newPlainText("password", info.getByKey("password"));
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(v.getContext(), "Скопированно в буффер обмена!", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            final NumberPicker picker = new NumberPicker(v.getContext());
                            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            picker.setLayoutParams(lp2);
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(v.getContext()).setTitle("Выберите время").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clipData = ClipData.newPlainText("password", info.getByKey("password"));
                                    clipboardManager.setPrimaryClip(clipData);
                                    TaskHelper.addListener(v.getContext(), new RemoveBufferTask(picker.getSelectedNumber()), new ITaskAnswerListener() {
                                        @Override
                                        public void onAnswer(Bundle data) {
                                            clipboardManager.setPrimaryClip(ClipData.newPlainText("password", ""));
                                            Toast.makeText(v.getContext(), "Пароль удален из буффера обмена", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Toast.makeText(v.getContext(), "Скопированно в буффер обмена с таймером " + picker.getSelectedNumber() + " секунд!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });
                            builder3.setView(picker).create().show();
                            break;
                    }
                }
            });
            builder.create().show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        name = (TextView) findViewById(R.id.tittle);
        symbol = (TextView) findViewById(R.id.oneSymbol);
        login = (EditText) findViewById(R.id.edit_login);
        password = (TextView) findViewById(R.id.password);
        image = (ImageView) findViewById(R.id.editText_password_visible);
        if (savedInstanceState != null && savedInstanceState.getParcelable(SecureInfo.TAG) != null) {
            info = savedInstanceState.getParcelable(SecureInfo.TAG);
        } else {
            info = new SecureInfo("New info");
        }

        name.setText(info.getName().toString());
        symbol.setText(String.valueOf(Character.toUpperCase(info.getName().charAt(0))));
        login.setText(info.getByKey("login"));
        password.setText((info.getByKey("password") != null) ? "****" : "Empty");
        password.setOnClickListener(onPasswordClick);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    password.setTransformationMethod(new SingleLineTransformationMethod());
                    image.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    password.setText(info.getByKey("password") == null || info.getByKey("password").length() == 0 ? "Empty" : info.getByKey("password"));
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    password.setText("****");
                    image.setImageResource(R.drawable.ic_visibility_black_24dp);
                }
            }
        });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.attachment_list);
        adapter = new AttachmentAdapter(this, this, info);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    File file = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    Log.i("MailActivity", file.getAbsolutePath());
                    info.addFile(file);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new MaterialFilePicker()
                            .withActivity(this)
                            .withRequestCode(FILE_SELECT)
                            .withHiddenFiles(true)
                            .start();
                }
                return;
            }

        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_RESPONSE, info);
        setResult(Constants.REQUEST_CODE_SECURE, intent);
        super.finish();
    }
}
