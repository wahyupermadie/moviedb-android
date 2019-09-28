package com.wepe.moviedb.koin

import com.wepe.moviedb.service.repository.SearchRepository
import com.wepe.moviedb.ui.searchActivity.SearchUseCases
import com.wepe.moviedb.ui.searchActivity.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchUseCases(get()) }
    factory { SearchRepository(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
}