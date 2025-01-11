package cz.mendelu.projek.di

import cz.mendelu.projek.communication.auth.AuthAPI
import cz.mendelu.projek.communication.auth.AuthRemoteRepositoryImpl
import cz.mendelu.projek.communication.auth.IAuthRemoteRepository
import cz.mendelu.projek.communication.board.BoardAPI
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
import cz.mendelu.projek.communication.board.IBoardRemoteRepository
import cz.mendelu.projek.communication.issue.IIssueRemoteRepository
import cz.mendelu.projek.communication.issue.IssueAPI
import cz.mendelu.projek.communication.issue.IssueRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRemoteRepository(authAPI: AuthAPI): IAuthRemoteRepository{
        return AuthRemoteRepositoryImpl(authAPI)
    }

    @Provides
    @Singleton
    fun provideBoardRemoteRepository(boardAPI: BoardAPI): IBoardRemoteRepository{
        return BoardRemoteRepositoryImpl(boardAPI)
    }

    @Provides
    @Singleton
    fun provideIssueRemoteRepository(issueAPI: IssueAPI): IIssueRemoteRepository{
        return IssueRemoteRepositoryImpl(issueAPI)
    }
}