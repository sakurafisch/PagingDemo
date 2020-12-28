package com.example.pagingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonPopulate;
    Button buttonClear;
    RecyclerView recyclerView;

    StudentDao studentDao;
    StudentsDatabase studentsDatabase;
    MyPagedAdapter pagedAdapter;
    LiveData<PagedList<Student>> allStudentsLivePaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPopulate = findViewById(R.id.buttonPopulate);
        buttonClear = findViewById(R.id.buttonClear);
        recyclerView = findViewById(R.id.recyclerView);
        pagedAdapter = new MyPagedAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(pagedAdapter);
        studentsDatabase = StudentsDatabase.getInstance(this);
        studentDao = studentsDatabase.getStudentDao();
        allStudentsLivePaged = new LivePagedListBuilder<>(studentDao.getAllStudents(), 30)
                .build();
        allStudentsLivePaged.observe(this, students -> pagedAdapter.submitList(students));
        buttonPopulate.setOnClickListener(v -> {
            Student[] students = new Student[1000];
            for (int i = 0; i < 1000; i++) {
                Student student = new Student();
                student.setStudentNumber(i);
                students[i] = student;
            }
            new Thread(() -> studentDao.insertStudents(students)).start();
        });
        buttonClear.setOnClickListener(v -> new Thread(() -> studentDao.deleteAllStudents()).start());
    }
}