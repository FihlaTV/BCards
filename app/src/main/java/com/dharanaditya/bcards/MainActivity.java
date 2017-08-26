package com.dharanaditya.bcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.dharanaditya.bcards.model.BCard;
import com.dharanaditya.bcards.ui.TestAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    // TODO Implement API service
    private RecyclerView bcardsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bcardsList = (RecyclerView) findViewById(R.id.rv_bcards_list);
        bcardsList.setLayoutManager(new LinearLayoutManager(this));
//        TODO Authenticate User

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
//                TODO Handle Firebase Sign Out
        }
        return super.onOptionsItemSelected(item);
    }

    void testRecyclerView() {
        List<BCard> bCards = new ArrayList<>();
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "https://media.licdn.com/mpr/mprx/0_0k9zITCg3oGCNW1q9z2v8CHykwTx-sDZx8CUSqcg34GOBo8M1zaR_PfyiWwiN4OZUQCR_vupWVhYlOHknc1div3r2Vh0lOXZ1c19HNqjFRzPUH0XxQ5ZehV2u7Vf4OPWsNBJ76WAGNh", ""));
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "https://media.licdn.com/mpr/mprx/0_0k9zITCg3oGCNW1q9z2v8CHykwTx-sDZx8CUSqcg34GOBo8M1zaR_PfyiWwiN4OZUQCR_vupWVhYlOHknc1div3r2Vh0lOXZ1c19HNqjFRzPUH0XxQ5ZehV2u7Vf4OPWsNBJ76WAGNh", ""));
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "", ""));
        TestAdapter testAdapter = new TestAdapter(bCards, this);
        bcardsList.setAdapter(testAdapter);
    }
    
//    TODO Merger to Master Branch
}
