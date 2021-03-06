package com.vary.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.vary.Models.CategoryModel;
import com.vary.Models.TeamModel;
import com.vary.R;
import com.vary.ViewModels.CardsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameSettingsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    private TextView amountCards;
    private TextView time;
    private SeekBar timeBar;
    private SeekBar amountCardsBar;
    private CardsViewModel viewModel;
    private ArrayAdapter<String> arrayAdapterTeams;
    private ArrayAdapter<String> arrayAdapterCategories;
    private ArrayList<String> mTeamsNames = new ArrayList<>();
    private ArrayList<String> mCategoriesNames = new ArrayList<>();
    private PenaltyType penalty;
    private int amountOfCards;
    private int roundDuration;
    private int startTeam;
    private int startCategory;
    private boolean steal = true;
    CallbackFragment fCallback;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_settings, container, false);

        Spinner cardDeckSpinner = view.findViewById(R.id.spinner_deck_card);
//        Button cardDeckButton = view.findViewById(R.id.button_deck_card);
//        cardDeckButton.setOnClickListener(this::onCardDeckButtonClick);

        arrayAdapterCategories = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, mCategoriesNames);
        arrayAdapterCategories.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        cardDeckSpinner.setAdapter(arrayAdapterCategories);
        cardDeckSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startCategory = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        amountOfCards = 50;
        roundDuration = 60;
        penalty = PenaltyType.lose_points;

        bindButton();

        amountCards = view.findViewById(R.id.amount_cards_dynamic);
        amountCards.setText(amountOfCards + getString(R.string.pieces));

        amountCardsBar = view.findViewById(R.id.bar_amount_cards);
        amountCardsBar.setOnSeekBarChangeListener(this);

        timeBar = view.findViewById(R.id.bar_round_time);
        timeBar.setOnSeekBarChangeListener(this);

        time = view.findViewById(R.id.time_round_dynamic);
        time.setText(roundDuration + getString(R.string.seconds));

        Spinner teamsSpinner = view.findViewById(R.id.choose_team_spinner);

        arrayAdapterTeams = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, mTeamsNames);
        arrayAdapterTeams.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        teamsSpinner.setAdapter(arrayAdapterTeams);
        teamsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTeam = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setObservers();
        SwitchCompat theftRightSwitch = view.findViewById(R.id.theft_right);
        theftRightSwitch.setOnCheckedChangeListener(this);

        RadioGroup penaltyGroup = view.findViewById(R.id.penalty_group);
        penaltyGroup.setOnCheckedChangeListener(this::onPenaltyGroupClicked);

        return view;
    }

    protected void setObservers() {
        Observer<List<TeamModel>> observer = teamModels -> {
            if (teamModels != null) {
                mTeamsNames = viewModel.getTeamsNames();
                arrayAdapterTeams.clear();
                arrayAdapterTeams.add(getResources().getString(R.string.random));
                arrayAdapterTeams.addAll(mTeamsNames);
                arrayAdapterTeams.notifyDataSetChanged();
            }
        };

        Observer<List<CategoryModel>> observerCategories = categoryModels -> {
            if (categoryModels != null) {
                mCategoriesNames = viewModel.getCategoriesNames();
                arrayAdapterCategories.clear();
                arrayAdapterCategories.addAll(mCategoriesNames);
                arrayAdapterCategories.notifyDataSetChanged();
            }
        };
        viewModel = new ViewModelProvider(requireActivity()).get(CardsViewModel.class);
        viewModel
                .getTeams()
                .observe(getViewLifecycleOwner(), observer);
        viewModel
                .getCategories()
                .observe(getViewLifecycleOwner(), observerCategories);


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.equals(amountCardsBar)) {
            progress /= getResources().getInteger(R.integer.card_amount_step);
            progress *= getResources().getInteger(R.integer.card_amount_step);
            progress += 10;
            amountOfCards = progress;
            amountCards.setText(amountOfCards + getString(R.string.pieces));

        } else if (seekBar.equals(timeBar)) {
            progress /= getResources().getInteger(R.integer.time_step);
            progress *= getResources().getInteger(R.integer.time_step);
            progress += 10;
            roundDuration = progress;
            time.setText(roundDuration + getString(R.string.seconds));

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        steal = isChecked;
    }

    void setCallback(CallbackFragment callback) {
        fCallback = callback;
    }

    void bindButton() {
        Button button = view.findViewById(R.id.start_game_button);
        button.setOnClickListener(v -> {
            Log.d("Settings", "round duration: " + roundDuration);
            if (startTeam == 0) {
                Random random = new Random();
                startTeam = random.nextInt(mTeamsNames.size());
            } else {
                startTeam--;
            }
            viewModel.setCurrentGame(startCategory, amountOfCards, roundDuration, penalty, steal, startTeam);
            viewModel.setRoundTimeLeft(roundDuration);
            viewModel.setRoundDuration(roundDuration);
            fCallback.callback(GameActions.start_game_process);
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void onPenaltyGroupClicked(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.no_penalty:
                penalty = PenaltyType.no_penalty;
                break;
            case  R.id.loss_points_penalty:
                penalty = PenaltyType.lose_points;
                break;
            case R.id.players_task_penalty:
                penalty = PenaltyType.players_task;
                break;
        }
    }
}