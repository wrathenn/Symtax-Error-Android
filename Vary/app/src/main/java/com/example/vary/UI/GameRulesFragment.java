package com.example.vary.UI;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vary.R;



public class GameRulesFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_rules, container, false);


//        TextView rulesView = view.findViewById(R.id.rules_game);
//        final SpannableStringBuilder text = new SpannableStringBuilder("1. Игроки разбиваются на команды.                    " +
//                "        2. Игрок из команды должен объяснить слово, изображенное на карточке своей команде за отведенное время." +
//                "        3. Однокоренные слова использовать запрещено (качели – качаются, водоросли – в воде), так же, как и переводы с другого языка." +
//                "        4. Если карточка отгадана, игрок объясняет следующую, пока есть время." +
//                "        5. За каждое отгаданное слово команда получает одно очко." +
//                "        6. Цель игры – набрать наибольшее количество очков.");
//
//        final ForegroundColorSpan style = new ForegroundColorSpan(Color.rgb(255, 135, 67));
//        final ForegroundColorSpan style2 = new ForegroundColorSpan(Color.rgb(255, 135, 67));
//
////        text.setSpan(style, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
////        text.setSpan(style2, 33, 35, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

//        rulesView.setText(text);


        return view;
    }
}