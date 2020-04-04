package com.example.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class admin_accounts_view extends AppCompatActivity
{
    DatabaseHelper dbh = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__accounts_view);

        generateListView();

        //attach listeners to filter controls (spinner & edittext)
        Spinner roleFilter = findViewById(R.id.spn_AdminViewRoleFilter);
        EditText nameFilter = findViewById(R.id.txt_AdminViewAllUsernameFilter);
        roleFilter.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //run getListview again
                generateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        });
        nameFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //run getListview again
                generateListView();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1)
        {
            generateListView(); // your "refresh" code
        }
    }

    private void generateListView()
    {
        //get filter inputs
        Spinner roleFilterspn = findViewById(R.id.spn_AdminViewRoleFilter);
        EditText nameFilter = findViewById(R.id.txt_AdminViewAllUsernameFilter);

        //with filter inputs, build sql query fragment
        String usernameFilter;
        if(nameFilter.getText().toString() == null || nameFilter.getText().toString().isEmpty())
        {usernameFilter = " IS NOT NULL";}
        else
        {usernameFilter = " LIKE '%"+nameFilter.getText().toString()+"%'";}
        String roleFilter;
        switch(roleFilterspn.getSelectedItemPosition())
        {
            case 1:
                roleFilter = " = 1";
                break;
            case 2:
                roleFilter = " = 2";
                break;
            case 3:
                roleFilter = " = 3";
                break;
            case 4:
                roleFilter = " = 4";
                break;
            default:
                roleFilter = " IS NOT NULL";
                break;
        }

        Cursor c = dbh.viewAllAccounts(usernameFilter,roleFilter);
        //create an array of accounts
        final ArrayList<account> accList = new ArrayList<>();
        if(c.getCount()>0)
        {
            //foreach account, create object equivalent and add into list
            while(c.moveToNext())
            {
                //add to list
                account ac = new account(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getInt(3));
                accList.add(ac);
            }
        }

        //generate listview
        ListView accountsView = findViewById(R.id.lsv_AdminAllAccounts);
        final accountsAdapter adapter = new accountsAdapter(this, accList);
        accountsView.setAdapter(adapter);
        //attach listview onItemClick listeners
        accountsView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                int ID = adapter.getItem(position).id;
                Intent detailActivity = new Intent(admin_accounts_view.this, admin_account_details.class);
                detailActivity.putExtra("targetID",ID);
                //need to refresh upon finishing detail activity
                startActivityForResult(detailActivity, 1);
            }
        });
    }

    //account model
    private class account
    {
        public int id;
        public String userName;
        public String fullName;
        public String role;

        //constructor requires input id, firstname, lastname and role (int)
        //role will be automatically translated into string.
        public account(int id, String username, String firstName, String lastName, int roleInt)
        {
            this.id = id;
            this.userName = (username);
            this.fullName =firstName + " " + lastName;
            switch(roleInt)
            {
                case 1:
                    this.role = "Admin";
                    break;
                case 2:
                    this.role = "Patient";
                    break;
                case 3:
                    this.role = "Doctor";
                    break;
                case 4:
                    this.role = "Cashier";
                    break;
            }
        }
    }

    //listview adapter class
    private class accountsAdapter extends ArrayAdapter<account>
    {
        public accountsAdapter(Context context, ArrayList<account> accs)
        { super(context, 0, accs); }

        @Override
        public View getView(int pos, View v, ViewGroup parent)
        {
            account acc = getItem(pos);
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.admin_account_list_item, parent, false);
            }
            //get components
            TextView accID = v.findViewById(R.id.txt_AdminAccListID);
            TextView accName = v.findViewById(R.id.txt_AdminAccListName);
            TextView accRole = v.findViewById(R.id.txt_AdminAccListRole);
            //fill component with data
            accID.setText((acc.userName));
            accName.setText(acc.fullName);
            accRole.setText(acc.role);
            // Return the view to render
            return v;
        }
    }
}
