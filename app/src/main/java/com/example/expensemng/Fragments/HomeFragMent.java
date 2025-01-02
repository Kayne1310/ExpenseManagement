package com.example.expensemng.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.IFireBase.ListBudgetCallBack;
import com.example.expensemng.IFireBase.ListExpenseCallBack;
import com.example.expensemng.IFireBase.OnUserCallBack;
import com.example.expensemng.Models.Budget;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.Models.User;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragMent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragMent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BarChart barChart;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private TextView userName, amount;
    private List<String> xValues = Arrays.asList("Last Month", "This Month");

    public HomeFragMent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragMent.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragMent newInstance(String param1, String param2) {
        HomeFragMent fragment = new HomeFragMent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_frag_ment, container, false);
        barChart = view.findViewById(R.id.barChart);
        userName = view.findViewById(R.id.userName);
        amount = view.findViewById(R.id.Amount);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadPage();
        return view;
    }


    void loadPage() {

        IgetInfor igetInfor = new firebaseService(db);
        igetInfor.getInforUser(user.getUid(), new OnUserCallBack() {
            @Override
            public void onSuccess(User user) {
                userName.setText(user.getUserName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        igetInfor.getBudget(user.getUid(), new ListBudgetCallBack() {
            @Override
            public void onSuccess(List<Budget> budgetList) {
                int totoalBudget = 0;
                Budget budget = new Budget();
                for (int i = 0; i < budgetList.size(); i++) {
                    budget = budgetList.get(i);
                    totoalBudget += budget.getAmount();

                }
                amount.setText(String.valueOf(totoalBudget) + " VND");
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        igetInfor.getExpense(user.getUid(), new ListExpenseCallBack() {
            @Override
            public void onSuccess(List<Expense> expenseList) {
                Expense expense = new Expense();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1 ;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                int currentMonthTotal = 0;
                int previousMonthToal = 0;

                for (int i = 0; i < expenseList.size(); i++) {
                    expense = expenseList.get(i);

                    try {
                        Date expenseDate = sdf.parse(expense.getDate());
                        Calendar expenseCalender = Calendar.getInstance();
                        expenseCalender.setTime(expenseDate);

                        int expenseYear = expenseCalender.get(Calendar.YEAR);
                        int expenseMotn = expenseCalender.get(Calendar.MONTH) + 1;

                        if (expenseYear == year && expenseMotn == month) {
                            currentMonthTotal += expense.getAmount();
                        } else if (expenseYear == year && expenseMotn == month - 1) {
                            previousMonthToal += expense.getAmount();
                        } else if (expenseYear == year - 1 && month == 1 && expenseMotn == 12) {
                            // Trường hợp đặc biệt: tháng 1 so với tháng 12 năm trước
                            previousMonthToal += expense.getAmount();
                        }
                        // Preparing data entries for last month and this month
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0, previousMonthToal));  // Chi tiêu tháng trước
                        entries.add(new BarEntry(1, currentMonthTotal));  // Chi tiêu tháng này

                        // Customizing Y-axis
                        YAxis yAxisLeft = barChart.getAxisLeft();
                        yAxisLeft.setAxisMinimum(0f);  // Bắt đầu từ 0
                        yAxisLeft.setDrawGridLines(false);  // Ẩn đường lưới
                        yAxisLeft.setDrawAxisLine(false);   // Ẩn trục Y bên trái
                        yAxisLeft.setEnabled(false);        // Không hiển thị nhãn bên trục Y trái

                        // Disabling right Y-axis
                        YAxis yAxisRight = barChart.getAxisRight();
                        yAxisRight.setEnabled(false);

                        // Customizing X-axis
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);  // Ẩn đường lưới dọc
                        xAxis.setDrawAxisLine(false);
                        xAxis.setGranularity(1f);       // Đảm bảo chỉ số không bị lặp
                        xAxis.setLabelCount(xValues.size());// Ẩn trục X

                        // Setting up data set
                        BarDataSet dataSet = new BarDataSet(entries, "");
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        dataSet.setValueTypeface(Typeface.SANS_SERIF);
                        dataSet.setValueTextSize(12f); // Hiển thị giá trị trên các thanh

                        // Tạo ValueFormatter tùy chỉnh để thêm "VND"
                        dataSet.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getBarLabel(BarEntry barEntry) {
                                float value = barEntry.getY();
                                if (value >= 1_000_000) {
                                    return String.format("%.1fM", value / 1000000); // Triệu
                                } else if (value >= 1_000) {
                                    return String.format("%.0fK", value / 1000); // Nghìn
                                } else {
                                    return String.valueOf((int) value); // Giá trị nguyên nếu nhỏ hơn 1000
                                }
                            }
                        });

                        // Assigning data to chart
                        BarData barData = new BarData(dataSet);
                        barData.setBarWidth(0.3f);
                        barChart.setData(barData);

                        // Disable chart description
                        barChart.getDescription().setEnabled(false);
                        barChart.getLegend().setEnabled(false);

                        // Refresh chart
                        barChart.invalidate();

                    }
                    catch (ParseException e) {


                    }


                }

            }

            @Override
            public void onFailed(Exception e) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        loadPage();

    }
}