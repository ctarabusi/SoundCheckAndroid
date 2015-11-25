package s2m.tryviperarchitecture.recordingusecase.di;

import javax.inject.Singleton;

import dagger.Component;
import s2m.tryviperarchitecture.recordingusecase.view.RecordPresenter;

/**
 * Created by cta on 15/09/15.
 */
@Singleton
@Component(modules = {RecordPresenterModule.class})
public interface RecordPresenterComponent
{
    RecordPresenter provideRecordPresenter();
}