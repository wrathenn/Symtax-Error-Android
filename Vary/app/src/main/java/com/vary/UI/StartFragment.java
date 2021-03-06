package com.vary.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import com.vary.Models.CurrentGameModel;
import com.vary.R;
import com.vary.ViewModels.CardsViewModel;

public class StartFragment extends Fragment {

    CallbackFragment fCallback;
    Button continue_game;
    View view;
    int width;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    public StartFragment() {
        // Required empty public constructor
    }


// some code

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.start_fragment_placeholder, container, false);
        int margin = width / 3;

        continue_game = view.findViewById(R.id.continue_game);
        continue_game.setOnClickListener(v -> fCallback.callback(GameActions.continue_game_action));
        MarginLayoutParams params = (MarginLayoutParams) continue_game.getLayoutParams();  // Настройка отступов кнопок
        params.setMarginEnd(margin);
        params.setMarginStart(margin);
        continue_game.setLayoutParams(params);
        Button new_game = view.findViewById(R.id.new_game);
        new_game.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            fCallback.callback(GameActions.new_game_action);
        });
        new_game.setLayoutParams(params);

        bindButton(R.id.rules, GameActions.open_rules);
        bindButton(R.id.settings, GameActions.open_settings);
        TextView textView = view.findViewById(R.id.app_title);
        textView.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            fCallback.callback(GameActions.open_rules);
        });

//        Button infobtn = view.findViewById(R.id.rules);
//        infobtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.startAnimation(buttonClick);
//                viewModel.getNewCategories();
//            }
//        });



//        checkContinueButtonVisibility(false);
        Observer<CurrentGameModel> observerGameModel = currentGameModel -> {
            Log.d("Model", "Gor value of model, it is void: " + currentGameModel.isVoid());
            //                    Toast toast = Toast.makeText(getContext(), "Model isn't void, info : " + currentGameModel.getCardModelList().size(), Toast.LENGTH_LONG);
            //                    toast.show();
            checkContinueButtonVisibility(!currentGameModel.isVoid());
        };

        CardsViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardsViewModel.class);

        viewModel
                .getGameModel()
                .observe(getViewLifecycleOwner(), observerGameModel);
//        checkContinueButtonVisibility(!viewModel.getGameModel().getValue().isVoid());

        return view;
    }

//    public void setViewModel(CardsViewModel viewModel) {
//        this.viewModel = viewModel;
//    }


    public void setWidth(int width) {
        this.width = width;
    } // Определение ширины текущего экрана

    void bindButton(int id, GameActions action) {
        Button button = view.findViewById(id);
        button.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            fCallback.callback(action);
        });
    }


    void setCallback(CallbackFragment callback) {
        fCallback = callback;
    }

    void checkContinueButtonVisibility(boolean visible) { //Настройка прозрачности кнопки "продолжить" в зависимости от наличия сохраненного состояния
        if (visible) {
            continue_game.setVisibility(View.VISIBLE);
        } else {
            continue_game.setVisibility(View.GONE);
        }
    }
}