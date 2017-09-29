package edu.upc.eseiaat.pma.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int ids_answers[]={
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    private int correct_answer;
    private int current_question;
    private String[] all_questions;
    private int[] user_answer;
    private TextView text_question;
    private boolean[] answer_is_correct;
    private RadioGroup group;
    private Button btn_next, btn_previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup) findViewById(R.id.answers_group);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_previous = (Button) findViewById(R.id.btn_previous);

        all_questions = getResources().getStringArray(R.array.all_questions);
        answer_is_correct = new boolean[all_questions.length];
        user_answer = new int[all_questions.length];
        for (int i=0; i<user_answer.length; i++){
            user_answer[i]=-1;
        }
        current_question = 0;
        show_question();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_answer();
                if (current_question < all_questions.length-1) {
                    current_question++;
                    show_question();
                }
                else {
                    int right=0, wrong=0;
                    for (boolean b : answer_is_correct){
                        if (b){
                            right++;
                        }
                        else {
                            wrong++;
                        }
                    }
                    String result = String.format("Right: %d -- Wrong: %d", right, wrong);
                    Toast.makeText(QuizActivity.this, result, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_answer();
                if (current_question > 0){
                    current_question--;
                    show_question();
                }
            }
        });

    }

    private void check_answer() {
        int active = group.getCheckedRadioButtonId();
        int answer = -1;
        for (int i=0; i<ids_answers.length; i++){
            int id = ids_answers[i];
            if (id==active){
                answer = i;
            }
        }

        answer_is_correct[current_question] = (answer==correct_answer);
        user_answer[current_question] = answer;
    }

    private void show_question() {
        String line = all_questions[current_question];
        String[] parts = line.split(";");

        group.clearCheck();

        text_question.setText(parts[0]);
        for (int i=0; i<ids_answers.length; i++){
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            String answer = parts[i+1];
            if (answer.charAt(0) == '*'){
                correct_answer = i;
                answer = answer.substring(1);
            }
            rb.setText(answer);
            if (user_answer[current_question] == i){
                rb.setChecked(true);
            }
        }

        if (current_question==0){
            btn_previous.setVisibility(View.INVISIBLE);
        } else {
            btn_previous.setVisibility(View.VISIBLE);
        }
        if (current_question == all_questions.length-1){
            btn_next.setText(R.string.finish);
        } else {
            btn_next.setText(R.string.next);
        }
    }
}
