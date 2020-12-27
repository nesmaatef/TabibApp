package com.example.tabibapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabibapp.Model.appoint;
import com.example.tabibapp.common.common;
import com.example.tabibapp.face.itemclicklistner;
import com.example.tabibapp.viewholder.appointmentviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class appoint_hospital extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseDatabase database3 ;
    DatabaseReference appointment3;
    RecyclerView recycler1;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<appoint, appointmentviewholder> adapter3;
    ImageView img;
    TextView txtday,txtfrom,txtto;
    appoint newappoint;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    String serviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_hospital);
        database3 = FirebaseDatabase.getInstance();
        appointment3 = database3.getReference("Rooms");
        recycler1 = (RecyclerView) findViewById(R.id.recycler1);
        img = (ImageView) findViewById(R.id.imgmore1);
        recycler1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler1.setLayoutManager(layoutManager);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog_hos();
            }
        });

        if (common.currentisstaff.equals("true")){
            img.setVisibility(View.INVISIBLE);
        }
        Intent intent = getIntent();
        serviceid = intent.getStringExtra("serviceid");
        loadhospitalappointments(serviceid);
    }
    private void showdialog_hos() {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(appoint_hospital.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_day, null);
        txtday=add_menu_layout.findViewById(R.id.txtday);
        txtfrom=add_menu_layout.findViewById(R.id.txtfrom);
        txtto=add_menu_layout.findViewById(R.id.txtto);

        alertdialog.setView(add_menu_layout);
        txtday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        txtfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(appoint_hospital.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        txtfrom.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });
        txtto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(appoint_hospital.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        txtto.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newappoint=new appoint();
                newappoint.setDay(txtday.getText().toString());
                newappoint.setFrom(txtfrom.getText().toString());
                newappoint.setTo(txtto.getText().toString());
                appointment3.child(serviceid).child("appointment").child(txtday.getText().toString()).setValue(newappoint); }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(appoint_hospital.this, "no date added", Toast.LENGTH_SHORT).show();

            }
        });
        alertdialog.show();

    }
    private void loadhospitalappointments(final String hospitalid) {
        adapter3=new FirebaseRecyclerAdapter<appoint, appointmentviewholder>(appoint.class,
                R.layout.appoint_item,
                appointmentviewholder.class,
                appointment3.child(hospitalid).child("appointment") ) {
            @Override
            protected void populateViewHolder(final appointmentviewholder appointmentviewholder,final appoint appoint, int i) {
                appointmentviewholder.value.setText(appoint.getDay());
                appointmentviewholder.value1.setText(appoint.getFrom());
                appointmentviewholder.value2.setText(appoint.getTo());

                common.currenthospital_date =appoint.getDay() ;
                common.currenthospital_date1 =appoint.getFrom() ;


                Toast.makeText(appoint_hospital.this, hospitalid+"null", Toast.LENGTH_SHORT).show();

                final String date22 = new SimpleDateFormat("E, MMM dd yyyy", Locale.getDefault()).format(new Date());
                Toast.makeText(appoint_hospital.this, "Current time =>"+ date22, Toast.LENGTH_SHORT).show();

                final appoint clickitem =appoint;
                final String string =appoint.getDay();


                appointmentviewholder.setItemClickListener(new itemclicklistner() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) throws ParseException {
                        try {
                            DateFormat formatter;
                            formatter = new SimpleDateFormat("E, MMM dd yyyy");
                            Date date = formatter.parse(date22);

                            DateFormat formatter1;
                            formatter1 = new SimpleDateFormat("E, MMM dd, yyyy");
                            Date  date33 = formatter1.parse(string);

                            if (date.before(date33)) {
                                Toast.makeText(appoint_hospital.this, "you can submit in this day", Toast.LENGTH_SHORT).show();

                            }
                            else if (date.after(date33)){
                                Toast.makeText(appoint_hospital.this, "you cannot submit in this day", Toast.LENGTH_SHORT).show();

                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }

        };

        recycler1.setAdapter(adapter3);


    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        txtday.setText(currentDateString);

    }
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(common.UPDATE)){
            updatedate(adapter3.getRef(item.getOrder()).getKey(),adapter3.getItem(item.getOrder()));
        }

        if (item.getTitle().equals(common.DELETE)){

            deletefood(adapter3.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void updatedate(final String key, final appoint item) {
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(appoint_hospital.this);
        LayoutInflater inflater =this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_day, null);
        txtday=add_menu_layout.findViewById(R.id.txtday);
        txtfrom=add_menu_layout.findViewById(R.id.txtfrom);
        txtto=add_menu_layout.findViewById(R.id.txtto);
        alertdialog.setView(add_menu_layout);
        txtday.setText(item.getDay());
        txtfrom.setText(item.getFrom());
        txtto.setText(item.getTo());
        txtday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        txtfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(appoint_hospital.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        txtfrom.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });
        txtto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(appoint_hospital.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        txtto.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setDay(txtday.getText().toString());
                item.setFrom(txtfrom.getText().toString());
                item.setTo(txtto.getText().toString());
                appointment3.child(serviceid).child("appointment").child(key).setValue(item); }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(appoint_hospital.this, "no date added", Toast.LENGTH_SHORT).show();

            }
        });
        alertdialog.show();


    }
    private void deletefood(String key) {
        appointment3.child(serviceid).child("appointment").child(key).removeValue();
    }


}
