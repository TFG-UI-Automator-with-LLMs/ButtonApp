package com.example.buttonapp.autotesting.util;

import java.util.List;
import java.util.ArrayList;

import com.example.buttonapp.autotesting.TestCase;
import com.example.buttonapp.autotesting.util.PredicateEvaluator;
import org.springframework.expression.spel.SpelEvaluationException;


public class TestPredicate {
    private List<String> clauses;

    public TestPredicate(String[] clauses){
        this.clauses=new ArrayList<String>();
        for(String clause:clauses){
            this.clauses.add(clause);
        }
    }

    public TestPredicate(List<String> clauses){
        this.clauses=clauses;
    }

    public TestPredicate(String predicate){
        this(predicate.split("&&"));
    }

    public boolean evaluate(TestCase testCase){
        return nClausesMeet(testCase)==clauses.size();
    }

    public int nClausesMeet(TestCase testCase){
        int result=0;
        PredicateEvaluator predicateEvaluator = new PredicateEvaluator();
        Boolean res;
        for(String clause:clauses) {
            try {
                res = predicateEvaluator.evaluate(clause,testCase);
                if (res)
                    result++;
            }catch(SpelEvaluationException ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return result;
    }

    public int getNClauses(){
        return clauses.size();
    }

    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        if(clauses.size()>1) {
            sb.append("(");
            sb.append(clauses.get(0));
            sb.append(")");
        } else if(clauses.size()==1)
            sb.append(clauses.get(0));
        for(int i=1;i<clauses.size();i++) {
            sb.append(" && (");
            sb.append(clauses.get(i));
            sb.append(")");
        }
        return sb.toString();
    }
}

