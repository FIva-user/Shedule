package com.fiva.shedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fiva.shedule.backend.DateUtils;
import com.fiva.shedule.backend.struct.Day;
import com.fiva.shedule.backend.struct.Lesson;
import com.fiva.shedule.backend.struct.Week;

public class SheduleAdapter extends RecyclerView.Adapter<SheduleAdapter.ViewHolder> {

    private Week week = new Week();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (week != null && week.getWeek() != null) {
            Day day = week.getWeek().get(position);

            {
                holder.textViewNumber1.setText("1");
                holder.textViewTime1.setText("");
                holder.textViewText1.setText("");
                holder.textViewText1and1.setText("");

                holder.textViewNumber2.setText("2");
                holder.textViewTime2.setText("");
                holder.textViewText2.setText("");
                holder.textViewText2and2.setText("");

                holder.textViewNumber3.setText("3");
                holder.textViewTime3.setText("");
                holder.textViewText3.setText("");
                holder.textViewText3and3.setText("");

                holder.textViewNumber4.setText("4");
                holder.textViewTime4.setText("");
                holder.textViewText4.setText("");
                holder.textViewText4and4.setText("");

                holder.textViewNumber5.setText("5");
                holder.textViewTime5.setText("");
                holder.textViewText5.setText("");
                holder.textViewText5and5.setText("");
            }


            holder.tvDayTitle.setText(day.getDate() + ", " + new DateUtils().getRussianDayOfWeek(day.getDate()));

            if (day.getLessons() != null) {
                for (Lesson lesson : day.getLessons()) {
                    if ("1".equals(lesson.getNumber())) {
                        holder.textViewTime1.setText(lesson.getTimeStart() + " - " + lesson.getTimeEnd());
                        holder.textViewText1.setText(lesson.getName() + " (" + lesson.getClassNumber() + ")");
                        holder.textViewText1and1.setText(lesson.getTeacher());
                    }

                    else if ("2".equals(lesson.getNumber())) {
                        holder.textViewTime2.setText(lesson.getTimeStart() + " - " + lesson.getTimeEnd());
                        holder.textViewText2.setText(lesson.getName() + " (" + lesson.getClassNumber() + ")");
                        holder.textViewText2and2.setText(lesson.getTeacher());
                    }

                    else if ("3".equals(lesson.getNumber())) {
                        holder.textViewTime3.setText(lesson.getTimeStart() + " - " + lesson.getTimeEnd());
                        holder.textViewText3.setText(lesson.getName() + " (" + lesson.getClassNumber() + ")");
                        holder.textViewText3and3.setText(lesson.getTeacher());
                    }

                    else if ("4".equals(lesson.getNumber())) {
                        holder.textViewTime4.setText(lesson.getTimeStart() + " - " + lesson.getTimeEnd());
                        holder.textViewText4.setText(lesson.getName() + " (" + lesson.getClassNumber() + ")");
                        holder.textViewText4and4.setText(lesson.getTeacher());
                    }

                    else if ("5".equals(lesson.getNumber())) {
                        holder.textViewTime5.setText(lesson.getTimeStart() + " - " + lesson.getTimeEnd());
                        holder.textViewText5.setText(lesson.getName() + " (" + lesson.getClassNumber() + ")");
                        holder.textViewText5and5.setText(lesson.getTeacher());
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (week != null && week.getWeek() != null) {
            return week.getWeek().size();
        } else {
            return 0;
        }
    }

    public void updateData(Week week) {
        this.week = week;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayTitle;
        TextView textViewNumber1;
        TextView textViewTime1;
        TextView textViewText1;
        TextView textViewText1and1;
        TextView textViewNumber2;
        TextView textViewTime2;
        TextView textViewText2;
        TextView textViewText2and2;
        TextView textViewNumber3;
        TextView textViewTime3;
        TextView textViewText3;
        TextView textViewText3and3;
        TextView textViewNumber4;
        TextView textViewTime4;
        TextView textViewText4;
        TextView textViewText4and4;
        TextView textViewNumber5;
        TextView textViewTime5;
        TextView textViewText5;
        TextView textViewText5and5;
        public ViewHolder(View view) {
            super(view);

            tvDayTitle = view.findViewById(R.id.tvDayTitle);

            textViewNumber1 = view.findViewById(R.id.tvLessonNum1);
            textViewTime1 = view.findViewById(R.id.tvLessonTime1);
            textViewText1 = view.findViewById(R.id.tvLessonName1);
            textViewText1and1 = view.findViewById(R.id.tvLessonName1_1);

            textViewNumber2 = view.findViewById(R.id.tvLessonNum2);
            textViewTime2 = view.findViewById(R.id.tvLessonTime2);
            textViewText2 = view.findViewById(R.id.tvLessonName2);
            textViewText2and2 = view.findViewById(R.id.tvLessonName2_2);

            textViewNumber3 = view.findViewById(R.id.tvLessonNum3);
            textViewTime3 = view.findViewById(R.id.tvLessonTime3);
            textViewText3 = view.findViewById(R.id.tvLessonName3);
            textViewText3and3 = view.findViewById(R.id.tvLessonName3_3);

            textViewNumber4 = view.findViewById(R.id.tvLessonNum4);
            textViewTime4 = view.findViewById(R.id.tvLessonTime4);
            textViewText4 = view.findViewById(R.id.tvLessonName4);
            textViewText4and4 = view.findViewById(R.id.tvLessonName4_4);

            textViewNumber5 = view.findViewById(R.id.tvLessonNum5);
            textViewTime5 = view.findViewById(R.id.tvLessonTime5);
            textViewText5 = view.findViewById(R.id.tvLessonName5);
            textViewText5and5 = view.findViewById(R.id.tvLessonName5_5);
        }
    }
}
