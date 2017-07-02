package com.example.karabushka.productlist;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Activity_log";
    private Button addButton;
    private EditText etProduct, etNumber;
    private RadioButton radVovka, radViktor, radSerozhka;
    private RecyclerFragment mRecyclerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(RecyclerFragment.FRAGMENT_TAG, "---------Activity onCreate-----------");
        addButton = (Button) findViewById(R.id.button);
        etProduct = (EditText) findViewById(R.id.editText);
        etNumber = (EditText) findViewById(R.id.editText2);
        radVovka = (RadioButton) findViewById(R.id.radioButtonVo);
        radViktor = (RadioButton) findViewById(R.id.radioButtonVi);
        radSerozhka = (RadioButton) findViewById(R.id.radioButtonS);

        if (getSupportFragmentManager().findFragmentByTag(RecyclerFragment.FRAGMENT_TAG) == null) {
            mRecyclerFragment = new RecyclerFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, mRecyclerFragment, RecyclerFragment.FRAGMENT_TAG);
            fragmentTransaction.commit();
            Log.d(RecyclerFragment.FRAGMENT_TAG, "Fragment is added");
        } else {
            mRecyclerFragment = (RecyclerFragment) getSupportFragmentManager().findFragmentByTag(RecyclerFragment.FRAGMENT_TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.remove_menu_item):
                mRecyclerFragment.removeAllProducts();
                break;
            case (R.id.exit_menu_item): {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String whatRBIsChecked() {
        String neighborName;
        if (radVovka.isChecked()) neighborName = RecyclerFragment.VOVKA;
        else if (radViktor.isChecked()) neighborName = RecyclerFragment.VICTOR;
        else neighborName = RecyclerFragment.SEROZHKA;
        return neighborName;
    }

    public void addProductToList(View view) {
        if (etNumber.getText().length() != 0 && etProduct.getText().length() != 0) {
            String product = etProduct.getText().toString();
            String number2 = etNumber.getText().toString();
            String neighborName = whatRBIsChecked();
            mRecyclerFragment.addProductToList(product, number2, neighborName);
            etProduct.getText().clear();
            etNumber.getText().clear();
        } else Toast.makeText(this, "Fill in everything", Toast.LENGTH_SHORT).show();
    }
}



