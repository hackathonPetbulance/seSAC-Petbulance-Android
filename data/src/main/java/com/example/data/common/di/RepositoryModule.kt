package com.example.data.common.di

import com.example.data.remote.local.preference.PreferenceRepositoryImpl
import com.example.data.repository.ExampleRepositoryImpl
import com.example.data.repository.feature.diagnosis.MockDiagnosisRepository
import com.example.data.repository.feature.hospital.MockHospitalRepository
import com.example.data.repository.feature.review.MockReviewRepository
import com.example.domain.repository.feature.ExampleRepository
import com.example.domain.repository.feature.diagnosis.DiagnosisRepository
import com.example.domain.repository.feature.hospital.HospitalRepository
import com.example.domain.repository.feature.review.ReviewRepository
import com.example.domain.usecase.nonfeature.preference.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

    @Binds
    abstract fun bindHospitalRepository(
        mock: MockHospitalRepository
//        impl: HospitalRepositoryImpl
    ): HospitalRepository

    @Binds
    abstract fun bindDiagnosisRepository(
        mock: MockDiagnosisRepository
//        impl: DiagnosisRepositoryImpl
    ): DiagnosisRepository

    @Binds
    abstract fun bindReviewRepository(
        mock: MockReviewRepository
//        impl: ReviewRepositoryImpl
    ): ReviewRepository
}