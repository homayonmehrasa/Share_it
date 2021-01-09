package ir.kurd.shareit.di

import ir.kurd.shareit.ui.installedapps.InstalledAppsVM
import ir.kurd.shareit.ui.main.MainVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{MainVM()}
    viewModel{InstalledAppsVM()}

}