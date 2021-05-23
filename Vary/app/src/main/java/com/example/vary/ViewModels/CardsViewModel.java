package com.example.vary.ViewModels;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vary.Models.CurrentGameModel;
import com.example.vary.Repositories.CategoriesRepo;
import com.example.vary.Repositories.TeamsRepo;
import com.example.vary.Repositories.CurrentGameRepo;
import com.example.vary.Database.DbManager;
import com.example.vary.UI.CardCallback;
import com.example.vary.UI.PenaltyType;
import com.example.vary.UI.GameMode;
import com.example.vary.UI.LoadDataCallback;
import com.example.vary.Network.LoadStatus;
import com.example.vary.Models.CardModel;
import com.example.vary.Models.CategoryModel;
import com.example.vary.Models.TeamModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CardsViewModel extends AndroidViewModel implements LoadDataCallback {
    private static final TeamsRepo mTeamsRepo = TeamsRepo.getInstance();
    private static final LiveData<List<TeamModel>> mTeams = mTeamsRepo.getTeams();
    private static final MutableLiveData<LoadStatus> mLoadStatus = new MutableLiveData<>();
    private static final CategoriesRepo mCategoriesRepo = CategoriesRepo.getInstance();
    private static final CurrentGameRepo gameRepo = CurrentGameRepo.getInstance();

    public void getNewCategories() {
        mCategoriesRepo.getNewCategories();
    }

    public ArrayList<String> getCategoriesNames() {
        return mCategoriesRepo.getCategoriesNames();
    }

    public CardsViewModel(@NonNull Application application) {
        super(application);
        mTeamsRepo.setDbManager(application);
        mCategoriesRepo.setNetworkService(application);
        mCategoriesRepo.setDbManager(application);
        gameRepo.setDbManager(application);
        mCategoriesRepo.setLoadCallback(this);
    }

    public void smth() {
        DbManager.getInstance(getApplication());
    }

    public void declineCard() {
        mCategoriesRepo.declineCard();
    }

    public void answerCard() {
        mCategoriesRepo.answerCard();
    }

    public void changeAnswerState(int pos) {
        mCategoriesRepo.changeAnswerState(pos);
    }

    public boolean getAnswerState(int pos) {
        return mCategoriesRepo.getAnswerState(pos);
    }

    public String getUsedCardByPosition(int pos) {
        return mCategoriesRepo.getUsedCardByPosition(pos);
    }

    public void setCardsCallback(CardCallback callback) {
        mCategoriesRepo.setCardsCallback(callback);
    }

    public int getAmountOfUsedCards() {
        return mCategoriesRepo.getAmountOfUsedCards();
    }

    public void newRoundMix() {
        mCategoriesRepo.newRoundMix();
    }

    public void fillCards(int index, int amount) {
        mCategoriesRepo.fillCards(index, amount);
    }

    public LiveData<List<CardModel>> getCards() {
        return mCategoriesRepo.getCards();
    }

    public void mixCards() {
        mCategoriesRepo.mixCards();
    }

    public String getCard() {
        return mCategoriesRepo.getCard();
    }

    public LiveData<List<TeamModel>> getTeams() {
        return mTeams;
    }

    public void removeTeams() {
        mTeamsRepo.removeTeams();
    }

    public void addTeams(String team_name) {
        mTeamsRepo.addTeam(team_name);
    }

    public void changeTeamPoints() {
        int points = mCategoriesRepo.countPoints();
        mTeamsRepo.increasePoints(points);
    }

    public void renameTeam(String name, int pos) {
        mTeamsRepo.renameTeam(name, pos);
    }

    public void saveState(SharedPreferences.Editor editor) {
        List<CardModel> mCardModels = CardsRepo.getInstance().getCards().getValue();
        if (mCardModels == null) {
            return;
        }
        // TODO очки команды ???
        gameRepo.saveState(mCardModels,
                            mTeamsRepo.getTeams().getValue(),
                            editor,
                            0,
                            CardsRepo.getInstance().getCurrentPosition(),
                            CardsRepo.getInstance().getStartRoundPosition(),
                            getTimerCount().getValue()
        );
    }

    public void sortTeamsByPoints() {
        mTeamsRepo.sortTeamsByPoints();
    }


    public void restoreState(SharedPreferences sp) {
        gameRepo.restoreState(sp);
    }


    public void setCurrentGame(int categoryIndex, int amountOfCards, int roundDuration, PenaltyType penalty, boolean steal, int startTeam) {
        mCategoriesRepo.fillCards(categoryIndex, amountOfCards);
        mCategoriesRepo.mixCards();
        mTeamsRepo.changeOrder(startTeam);
        gameRepo.setGameModel(steal, penalty, roundDuration);
    }

    public boolean endGame() {
        if (mCategoriesRepo.newRoundRequired() && gameRepo.getGameMode() == GameMode.one_word_mode)
            return true;
        return false;
    }

    public boolean nextRound() {
        mTeamsRepo.changeOrder(1);
        if (mCategoriesRepo.newRoundRequired()) {
            Log.d("Cards", "In mix required");
            mCategoriesRepo.mixCards();
            return gameRepo.nextGameMode();
        }
        else {
            newRoundMix();
        }
        return true;
    }

    public PenaltyType getPenalty() {
        return gameRepo.getPenalty();
    }

    public GameMode getGameMode() {
        return gameRepo.getGameMode();
    }

    public boolean getSteal() {
        return gameRepo.getSteal();
    }

    public void setCurrentRoundPoints(int points) {
        gameRepo.setCurrentRoundPoints(points);
    }

    public void setRoundTimeLeft(int time) {
        gameRepo.setRoundTimeLeft(time);
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return mCategoriesRepo.getCategories();
    }

    public void getCount(DbManager.CountListener listener) {
        mCategoriesRepo.getCount(listener);
    }

    public LiveData<CurrentGameModel> getGameModel() {
        return gameRepo.getGameModel();
    }

    public LiveData<LoadStatus> getLoadStatus() {
        return mLoadStatus;
    }

    public ArrayList<String> getTeamsNames() {
        return mTeamsRepo.getTeamsNames();
    }

    public int getSize() {
        return mTeamsRepo.getSize();
    }

    public String getCurTeamName(int pos) {
        return mTeamsRepo.getTeamName(pos);
    }

    public void removeTeam(int pos) {
        mTeamsRepo.removeTeam(pos);
    }

    public void onLoad(Throwable throwable, boolean notify) {
        mLoadStatus.postValue(new LoadStatus(throwable, notify));
    }

    public int getAmountOfTeams() {
        return mTeamsRepo.getSize();
    }

    public int getAmountOfCards() {
        return mCategoriesRepo.getAmountOfCards();
    }

    public String getTeamName(int position) {
        return mTeamsRepo.getTeamName(position);
    }

    public int getTeamPoints(int position) {
        return mTeamsRepo.getTeamPoints(position);
    }

    public int getRoundDuration() {
        return gameRepo.getRoundDuration();
    }

    /*
    Таймер
     */
    private static final MutableLiveData<Integer> mTimerCount = new MutableLiveData<>();

    public void setTimerCount(Integer seconds) {
        mTimerCount.postValue(seconds);
    }

    public LiveData<Integer> getTimerCount() {
        return mTimerCount;
    }
}
