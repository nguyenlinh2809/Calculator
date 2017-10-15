package com.example.linh0nguyen.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtKetqua;
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }

    private void addControls() {
        int []idButton = {R.id.btn00, R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9, R.id.btnCopy, R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiple, R.id.btnDivide,
                R.id.btnDot, R.id.btnDelete, R.id.btnNgoacPhai, R.id.btnNgoacTrai, R.id.btnEqual, R.id.btnPercent

        };
        for(int i:idButton){
            View v = findViewById(i);
            v.setOnClickListener(this);
        }
        edtKetqua = (EditText) findViewById(R.id.edtKetqua);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEqual:
                String[] s = xuliChuoi(result);
                String[] a = convertToPostFix(s);
                String kq = calculator(a);
                edtKetqua.setText(result+" = "+kq);
                break;
            case R.id.btnDelete:
                result = "";
                edtKetqua.setText("0");
                break;
            default:
                result += ((Button)v).getText().toString();
                edtKetqua.setText(result+"");
                break;
        }
    }

    public int getPriority(char c){
        if(c == '+' || c == '-'){
            return 1;
        }else if(c == '*' || c == '/'){
            return 2;
        }else return 0;
    }
    public boolean isOperator(char c){
        char operator[] = {'+', '-', '*', '/', '%', '(', ')'};
        Arrays.sort(operator);
        if(Arrays.binarySearch(operator, c)> -1){
            return true;
        }
        else return false;
    }

    public String[] xuliChuoi(String chuoi){
        String s = "";
        String phanTuChuoi[] = null;
        chuoi = chuoi.replaceAll("\\s+", " ");
        for(int i=0; i<chuoi.length(); i++){
            char c = chuoi.charAt(i);
            if(!isOperator(c)){
                s += c;
            }
            else s = s+" "+c+" ";
        }
        s = s.trim();
        s = s.replaceAll("\\s+", " ");
        phanTuChuoi = s.split(" ");
        return phanTuChuoi;
    }
    public String[] convertToPostFix(String []chuoi){
        String s1 = "", E[]=null;
        Log.d("chuoi", chuoi.length+"");
        Stack<String> stack = new Stack<String>();
        if(chuoi != null && chuoi.length > 0){
            for(int i=0; i<chuoi.length; i++){
                char c = chuoi[i].charAt(0);
                if(!isOperator(c)){
                    s1 = s1 + chuoi[i]+ " ";
                }else {
                    if(c=='('){
                        stack.push(chuoi[i]);
                    }else {
                        if(c == ')'){
                            char c1;
                            do {
                                c1 = stack.peek().charAt(0);
                                if (c1 != '(') s1 = s1 + stack.peek() + " ";
                                stack.pop();
                            }while (c1 != '(');
                        }else {
                            while (!stack.isEmpty() && (getPriority(stack.peek().charAt(0))>= getPriority(c))){
                                s1 = s1 + stack.peek()+ " ";
                                stack.pop();
                            }
                            stack.push(chuoi[i]);
                        }
                    }
                }
            }
            while (!stack.isEmpty()){
                s1 = s1 + " " + stack.peek();
                stack.pop();
            }
            s1.trim();
            s1 = s1.replaceAll("\\s+", " ");
            E = s1.split(" ");
        }

        return E;
    }
    public String calculator(String[] postFix){
        Stack <String> S = new Stack<String>();
        if(postFix.length > 0 && postFix != null){
            for (int i=0; i<postFix.length; i++){
                System.out.println(i + ":" + postFix[i]);

                char c = postFix[i].charAt(0);
                if (!isOperator(c)){
                    S.push(postFix[i]);
                }
                else{
                    double num = 0f;
                    double num1 = Float.parseFloat(S.pop());
                    double num2 = Float.parseFloat(S.pop());
                    switch (c) {
                        case '+' : num = num2 + num1; break;
                        case '-' : num = num2 - num1; break;
                        case '*' : num = num2 * num1; break;
                        case '/' : num = num2 / num1; break;
                        case '%' : num = num2 % num1; break;
                        default:
                            break;
                    }
                    S.push(Double.toString(num));
                }
            }
        }

        return S.pop();
    }
}
