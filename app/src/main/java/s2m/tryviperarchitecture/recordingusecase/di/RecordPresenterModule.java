package s2m.tryviperarchitecture.recordingusecase.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import s2m.tryviperarchitecture.recordingusecase.interactor.RecordInteractor;
import s2m.tryviperarchitecture.recordingusecase.view.RecordPresenter;

@Module
public class RecordPresenterModule
{
    private final Context context;

    public RecordPresenterModule(Context context)
    {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext()
    {
        return context;
    }

    @Provides
    @Singleton
    RecordInteractor provideRecordInteractor(Context applicationContext)
    {
        return new RecordInteractor(applicationContext);
    }

    @Provides
    @Singleton
    RecordPresenter provideRecordPresenter(RecordInteractor interactor)
    {
        return new RecordPresenter(interactor);
    }
}