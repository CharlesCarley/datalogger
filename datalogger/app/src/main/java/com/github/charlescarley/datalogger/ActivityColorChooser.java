/*
-------------------------------------------------------------------------------

    2019 Charles Carley.

    Advanced Programming: Java - 24569 - CSCI 310-001 - Fall 2019

-------------------------------------------------------------------------------
  This software is provided 'as-is', without any express or implied
  warranty. In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.
-------------------------------------------------------------------------------
*/
package com.github.charlescarley.datalogger;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SeekBar;

import androidx.lifecycle.ViewModelProviders;

import com.github.charlescarley.datalogger.models.ColorViewModel;
import com.github.charlescarley.datalogger.repositories.ColorRepository;
import com.github.charlescarley.datalogger.room.ColorElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ActivityColorChooser extends ActivityBase implements
    SeekBar.OnSeekBarChangeListener,
    ColorAdapter.ColorClickListener
{
    SeekBar m_red, m_green, m_blue;
    View         m_color;
    GridView     m_storage;
    ColorAdapter m_adapter;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_color);

        setActionBarTitle(R.string.act_add_new_color_title, false);
        int color = getIntent().getIntExtra(Codes.ARG1, getColor(R.color.colorAccentDark));

        m_color = findViewById(R.id.color);
        m_color.setBackgroundColor(color);

        m_red = findViewById(R.id.m_redSlider);
        m_red.setProgress(Color.red(color));

        m_green = findViewById(R.id.m_greenSlider);
        m_green.setProgress(Color.green(color));

        m_blue = findViewById(R.id.m_blueSlider);
        m_blue.setProgress(Color.blue(color));

        m_red.setOnSeekBarChangeListener(this);
        m_green.setOnSeekBarChangeListener(this);
        m_blue.setOnSeekBarChangeListener(this);


        m_adapter = new ColorAdapter(this, this);
        m_storage = findViewById(R.id.storage);
        m_storage.setAdapter(m_adapter);

        ViewModelProviders.of(this).
            get(ColorViewModel.class).
            getObservable().
            observe(this, this::updateAdapter);
        ColorRepository.get().access();
    }


    private void updateAdapter( List<ColorElement> colorElements )
    {

        m_adapter.updateElements(colorElements);
        m_storage.setAdapter(m_adapter);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuItem item = menu.add(0, R.id.CLEAR, 0, getString(R.string.menu_clear));
        item.setIcon(R.drawable.ic_broom);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setOnMenuItemClickListener(this::onClear);


        item = menu.add(0, R.id.RANDOM, 0, getString(R.string.menu_random_palette));
        item.setOnMenuItemClickListener(this::createRandomPalette);


        return super.onCreateOptionsMenu(menu);
    }


    private boolean createRandomPalette( MenuItem menuItem )
    {

        Random r = new Random();
        List<ColorElement> colors = new ArrayList<>(20);

        for (int i = 0; i < Codes.GEN_MAX; ++i)
        {
            ColorElement elm = new ColorElement();
            elm.setColor(Color.argb(255, r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            colors.add(elm);
        }

        ColorRepository.get().push(colors);
        return true;
    }


    private boolean onClear( MenuItem menuItem )
    {

        ColorRepository.get().clear();

        // consume and prevent further execution
        return true;
    }


    public void onOkClicked( View view )
    {

        setResult(progressToColor());
        finish();
    }


    public void onCancelClicked( View view )
    {

        setResult(Codes.CODE_CANCEL);
        finish();
    }


    @Override
    public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser )
    {

        m_color.setBackgroundColor(progressToColor());
    }


    @Override
    public void onStartTrackingTouch( SeekBar seekBar )
    {

    }


    @Override
    public void onStopTrackingTouch( SeekBar seekBar )
    {

    }


    @Override
    public void onClick( ColorElement item )
    {

        int color = item.getColor();
        m_color.setBackgroundColor(color);

        m_red.setProgress(Color.red(color));
        m_green.setProgress(Color.green(color));
        m_blue.setProgress(Color.blue(color));
    }


    private int progressToColor()
    {

        return Color.argb(255,
                          m_red.getProgress(),
                          m_green.getProgress(),
                          m_blue.getProgress());
    }


    public void addToCustom( View view )
    {

        int c = progressToColor();
        ColorElement ent = new ColorElement();
        ent.setColor(c);
        ColorRepository.get().push(ent);
    }
}
