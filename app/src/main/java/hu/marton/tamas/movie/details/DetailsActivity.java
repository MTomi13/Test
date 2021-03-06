package hu.marton.tamas.movie.details;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import hu.marton.tamas.movie.MovieActivity;
import hu.marton.tamas.movie.R;
import hu.marton.tamas.movie.api.Popular.model.ResultWrapper;

/**
 * Created by tamas.marton on 27/05/2016.
 */
public class DetailsActivity extends MovieActivity {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.collapsing_image)
    ImageView collapsingImage;

    @BindView(R.id.poster_imageview)
    ImageView posterImage;

    @BindView(R.id.original_language)
    TextView originalLangTextView;

    @BindView(R.id.overview)
    TextView overViewTextView;

    @BindView(R.id.rate)
    TextView rateTextView;

    @BindView(R.id.original_title)
    TextView titleTextView;

    @BindView(R.id.release_date)
    TextView releaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setupToolbar();

        ResultWrapper resultWrapper = getIntent().getParcelableExtra(ResultWrapper.class.getName());
        setupImagesView(resultWrapper);
        setupTextViews(resultWrapper);
    }

    /**
     * setup the "back button" functionality for the toolbar
     */
    private void setupToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param resultWrapper resultWrapper
     */
    private void setupImagesView(ResultWrapper resultWrapper) {
        setupImages(collapsingImage, resultWrapper.getBackDropImageUrl());
        setupImages(posterImage, resultWrapper.getLogoImageUrl());
    }

    private void setupImages(ImageView imageView, String url) {
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imageView);
        Picasso.with(this).setIndicatorsEnabled(true);
    }

    /**
     * @param resultWrapper resultWrapper
     *                      setup the textViews
     */
    private void setupTextViews(ResultWrapper resultWrapper) {
        originalLangTextView.setText(getResources().getString(R.string.original_language, resultWrapper.getOriginalLanguage()));
        overViewTextView.setText(resultWrapper.getOverview());
        rateTextView.setText(getResources().getString(R.string.rate, String.valueOf(resultWrapper.getVoteAverage()), resultWrapper.getVoteCount()));
        setupViewsByContentType(resultWrapper);
    }

    /**
     * @param resultWrapper resultWrapper
     *                      setup the views by contentType. There could be differences between Movies and Series.
     */
    private void setupViewsByContentType(ResultWrapper resultWrapper) {
        switch (resultWrapper.getContentType()) {
            case MOVIES:
                collapsingToolbar.setTitle(resultWrapper.getTitle());
                titleTextView.setText(getResources().getString(R.string.original_title, resultWrapper.getOriginalTitle()));
                releaseDateTextView.setText(resultWrapper.getReleaseDate());
                break;
            case SERIES:
                collapsingToolbar.setTitle(resultWrapper.getName());
                setupOriginCountryForSeries(resultWrapper);
                titleTextView.setText(getResources().getString(R.string.original_title, resultWrapper.getOriginalName()));
                releaseDateTextView.setText(resultWrapper.getFirstAirDate());
                break;
        }
    }

    private void setupOriginCountryForSeries(ResultWrapper resultWrapper) {
        List<String> originCountry = resultWrapper.getOriginCountry();
        if (originCountry.size() != 0) {
            ((TextView) findViewById(R.id.origin_country)).setText(getResources().getString(R.string.origin_country, originCountry.get(0)));
        }
    }
}
