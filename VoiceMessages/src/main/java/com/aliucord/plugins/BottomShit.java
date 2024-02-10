package com.aliucord.plugins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.widgets.BottomSheet;
import com.discord.views.CheckedSetting;
import com.discord.views.RadioManager;

import java.util.Arrays;
import java.util.List;

public class BottomShit extends BottomSheet {

    SettingsAPI settings;

    public BottomShit(SettingsAPI settings) {
        this.settings = settings;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Context context = requireContext();
        setPadding(20);

        CheckedSetting highSamplingRate = Utils.createCheckedSetting(context, CheckedSetting.ViewType.CHECK, "Increases sampling rate", "This might fix speed up sound problem");
        highSamplingRate.setChecked(settings.getBool("highSamplingRate", false));

        highSamplingRate.setOnCheckedListener(aBoolean -> {
            settings.setBool("highSamplingRate", aBoolean);
        });

        addView(highSamplingRate);

        List<CheckedSetting> radios = Arrays.asList(
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "High", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Normal", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Low", null)
        );

        RadioManager radioManager = new RadioManager(radios);

        List<Integer> bitDepth = Arrays.asList(192, 128, 64);

        CheckedSetting selectedRadio = radios.get(bitDepth.indexOf(settings.getInt("audioQuality", 128)));

        selectedRadio.setChecked(true);
        radioManager.a(selectedRadio);

        int radioSize = radios.size();

        for (int i = 0; i < radioSize; i++) {
            int finalSize = i;
            CheckedSetting radio = radios.get(i);
            radio.e(e -> {
                settings.setInt("audioQuality", bitDepth.get(finalSize));
                radioManager.a(radio);
            });

            addView(radio);
        }

        radios = Arrays.asList(
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "1000ms", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "800ms", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "500ms", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "200ms", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "50ms", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "0ms", null)
        );

        RadioManager radioManager2 = new RadioManager(radios);

        List<Integer> waitMs = Arrays.asList(1000, 800, 500, 200, 50, 0);

        selectedRadio = radios.get(waitMs.indexOf(settings.getInt("waitMs", 200)));

        selectedRadio.setChecked(true);
        radioManager2.a(selectedRadio);

        radioSize = radios.size();

        for (int i = 0; i < radioSize; i++) {
            int finalSize = i;
            CheckedSetting radio = radios.get(i);
            radio.e(e -> {
                settings.setInt("waitMs", waitMs.get(finalSize));
                radioManager2.a(radio);
            });

            addView(radio);
        }

    }
}
