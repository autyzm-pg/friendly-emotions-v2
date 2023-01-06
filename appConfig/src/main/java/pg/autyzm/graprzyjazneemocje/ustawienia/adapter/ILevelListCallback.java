package pg.autyzm.graprzyjazneemocje.ustawienia.adapter;

public interface ILevelListCallback {

    void editLevel(LevelItem level);

    void removeLevel(LevelItem level);

    void setLevelActive(LevelItem level, boolean isChecked, boolean isLearnMode);
}
