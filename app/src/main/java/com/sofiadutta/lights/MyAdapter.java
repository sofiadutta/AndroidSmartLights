package com.sofiadutta.lights;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sofiadutta.R;
import com.sofiadutta.SHACApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[][] mDataset;
    private KasaInfo kasaInfo;
    private String userInfo;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(KasaInfo kasaInfo, String userInfo) {
//        for(int i = 0; i < kasaInfo.getLights().length; i++){
//            for(int j = 0; j < kasaInfo.getLights()[i].length; j++){
//                Log.v("Sofia info", kasaInfo.getLights()[i][j]);
//            }
//        }
//        Log.v("Sofia info", "got out!");
        mDataset = kasaInfo.getLights();
        this.kasaInfo = kasaInfo;
        this.userInfo = userInfo;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_children, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position][0]);
        if (mDataset[position][1].charAt(0) == ':') {
            Log.v("Sofia what did we get?", "'"+mDataset[position][1].substring(1)+"'");
            holder.mSwitch.setChecked(Integer.parseInt(mDataset[position][1].substring(1)) == 1);
        } else {
            Log.v("Sofia what did we get in else?", mDataset[position][1]);
            holder.mSwitch.setChecked(Integer.parseInt(mDataset[position][1]) == 1);
        }

        if (userInfo.equals(SHACApplication.getAdultFamilyMember())) {
            try {
                Date currentTime = Calendar.getInstance().getTime();
                String currentTimeStr = currentTime.toString();

                String stringDate = currentTimeStr.substring(8, 10);
                String stringMonth = currentTimeStr.substring(4, 7);
                String stringYear = currentTimeStr.substring(24);
                String stringTime = currentTimeStr.substring(11, 19);

                final String tenPM = "00:00:00";
                final String sevenAM = "07:00:00";

                String string1 = stringDate + "/" + stringMonth + "/" + stringYear + " " + tenPM;
                Date time1 = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);

                String nextDay = Integer.toString(Integer.parseInt(stringDate) + 1);

                String string2 = nextDay + "/" + stringMonth + "/" + stringYear + " " + sevenAM;
                Date time2 = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").parse(string2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);

                currentTimeStr = stringDate + "/" + stringMonth + "/" + stringYear + " " + stringTime;
                Date x = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").parse(currentTimeStr);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(x);
                calendar3.add(Calendar.DATE, 1);

//                Log.d("timePrajitIsBefore", Boolean.toString(x.after(time1)));
//                Log.d("timePrajitIsAfter", Boolean.toString(x.before(time2)));
//                Log.d("timePrajitIsCheck", x.toString());
//                Log.d("timePrajitIsStart", time1.toString());
//                Log.d("timePrajitIsEnd", time2.toString());
                if (x.after(time1) && x.before(time2)) {
                    final String temporalString = "Reason: Rule 7 - \"Between " + tenPM + " hours and " +
                            sevenAM + " hours, access to device is denied\"";
                    //checks whether the current time is between 14:49:00 and 20:11:13.
                    holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            buttonView.setChecked(!isChecked);
                            Snackbar snackbar = Snackbar.make(buttonView, temporalString,
                                    Snackbar.LENGTH_LONG);

                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(buttonView.getResources().getColor(
                                    R.color.colorPrimary, buttonView.getContext().getTheme()));
                            snackbar.show();
                        }
                    });
                } else {
                    Log.d("timePrajitIsCheckPass", "False");
                    holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ChangeStateTask changeStateTask = new ChangeStateTask();
                            changeStateTask.execute(position);
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    buttonView.setChecked(!isChecked);
                    Snackbar snackbar = Snackbar.make(buttonView, R.string.read_only_access,
                            Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(buttonView.getResources().getColor(
                            R.color.colorPrimary, buttonView.getContext().getTheme()));
                    snackbar.show();
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private Switch mSwitch;

        public ViewHolder(View v) {
            super(v);

            mTextView = v.findViewById(R.id.text_title);
            mSwitch = v.findViewById(R.id.toggle);
        }
    }

    private class ChangeStateTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... positions) {
            try {
                int position = positions[0];
                kasaInfo.changeState(position);
            } catch (Exception e) {
                Log.e("LoginActivity", e.toString());
            }
            return false;
        }
    }
}