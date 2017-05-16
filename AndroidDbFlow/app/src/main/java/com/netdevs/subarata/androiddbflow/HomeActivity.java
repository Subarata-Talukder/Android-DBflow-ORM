package com.netdevs.subarata.androiddbflow;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.netdevs.subarata.androiddbflow.database.BioDb;
import com.netdevs.subarata.androiddbflow.database.InitiateBioDb;
import com.netdevs.subarata.androiddbflow.model.Bio;
import com.netdevs.subarata.androiddbflow.model.Bio_Table;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private TextView txtErr;
    private EditText etxtName;
    private EditText etxtEmail;
    private EditText etxtCell;
    private EditText etxtEdu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnSave = (Button) findViewById(R.id.btn_save);
        Button btnGet = (Button) findViewById(R.id.btn_get);
        txtErr = (TextView) findViewById(R.id.txtErr);
        etxtName = (EditText) findViewById(R.id.etxt_name);
        etxtEmail = (EditText) findViewById(R.id.etxt_email);
        etxtCell = (EditText) findViewById(R.id.etxt_cell);
        etxtEdu = (EditText) findViewById(R.id.etxt_edu);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bio bio = new Bio();
                DatabaseDefinition db = FlowManager.getDatabase(BioDb.class);

                bio.setName(etxtName.getText().toString());
                bio.setCell(etxtEmail.getText().toString());
                bio.setEducation(etxtCell.getText().toString());
                bio.setEmail(etxtEdu.getText().toString());

                saveDataAsync(db, bio);
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etxtName.setText("");
                etxtCell.setText("");
                etxtEdu.setText("");

                Bio getInfo = getDataSingle(etxtEmail.getText().toString().trim());
                if (getInfo == null) return;

                txtErr.setTextColor(Color.parseColor("#000000"));
                txtErr.setText("User info : \nName :-" + getInfo.getName() +
                        " \nEmail :-" + getInfo.getEmail() +
                        " \nCell :-" + getInfo.getCell() +
                        " \nEducation :-" + getInfo.getEducation());

            }
        });
    }

    private void clearFields() {
        etxtName.setText("");
        etxtEmail.setText("");
        etxtCell.setText("");
        etxtEdu.setText("");
    }

    // Asynchronous transactions easily, with expressive builders
    private void saveDataAsync(final DatabaseDefinition db, final Bio bio) {
        db.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // do something in BG
                bio.save(databaseWrapper); // use wrapper (from BaseModel)
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(Transaction transaction) {
                txtErr.setTextColor(Color.parseColor("#0da2ec"));
                txtErr.setText("Save data successfully");
                clearFields();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                txtErr.setTextColor(Color.parseColor("#f20606"));
                txtErr.setText("Save Error " + error.getMessage().toString());
            }
        }).build().execute();
    }

    private void saveData(Bio bio) {
        bio.save();
    }

    // Synchronous Transactions
    private void saveDataSync(final Bio bio) {
        FlowManager.getDatabase(BioDb.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                bio.save(databaseWrapper);
            }
        });
    }

    // Asyncly recommend queries on this queue to help prevent locking and threading issues when using a database.
    private Bio getDataAsync(String email) {
        final Bio[] bio = new Bio[1];
        try {
            SQLite.select()
                    .from(Bio.class)
                    .where(Bio_Table.email.is(email.trim()))
                    .async()
                    .queryResultCallback(new QueryTransaction.QueryResultCallback<Bio>() {
                        @Override
                        public void onQueryResult(QueryTransaction<Bio> transaction, @NonNull CursorResult<Bio> tResult) {
                            bio[0] = (Bio) tResult.toModel();

                            if (bio[0] != null)
                                Log.d("Education", "" + bio[0].getEducation());
                            tResult.close();
                        }
                    }).execute();
        } catch (Exception ex) {
        }

        return bio[0];
    }

    // list
    private List<Bio> getDataList() {
        try {
            return SQLite.select()
                    .from(Bio.class)
                    .queryList();
        } catch (Exception ex) {
        }
        return null;
    }

    private Bio getDataSingle(String email) {
        try {
            // single result, we apply a limit(1) automatically to get the result even faster.
            return SQLite.select()
                    .from(Bio.class)
                    .where(Bio_Table.email.eq(email.trim()))
                    .querySingle();
        } catch (Exception ex) {
        }
        return null;
    }
}
