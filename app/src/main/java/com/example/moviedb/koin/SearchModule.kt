package com.example.moviedb.koin

import com.example.moviedb.service.repository.SearchRepository
import com.example.moviedb.ui.searchActivity.SearchUseCases
import com.example.moviedb.ui.searchActivity.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchUseCases(get()) }
    factory { SearchRepository(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
}