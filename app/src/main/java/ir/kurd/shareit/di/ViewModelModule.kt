package ir.kurd.shareit.di

import ir.kurd.shareit.ui.filemanager.FileManagerVM
import ir.kurd.shareit.ui.images.ImagesVM
import ir.kurd.shareit.ui.installedapps.InstalledAppsVM
import ir.kurd.shareit.ui.main.MainVM
import ir.kurd.shareit.ui.music.MusicVM
import ir.kurd.shareit.ui.prepare.SendPrepareVM
import ir.kurd.shareit.ui.test.TestP2PVM
import ir.kurd.shareit.ui.music.MusicplayerVM
import ir.kurd.shareit.ui.video.VideoPlayerFragment
import ir.kurd.shareit.ui.video.VideoPlayerVM
import ir.kurd.shareit.ui.video.VideoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{MainVM()}
    viewModel{InstalledAppsVM()}
    viewModel { FileManagerVM() }
    viewModel { ImagesVM() }
    viewModel { MusicVM() }
    viewModel { VideoVM() }
    viewModel { SendPrepareVM() }
    viewModel { TestP2PVM() }
    viewModel { MusicplayerVM() }
    viewModel { VideoPlayerVM() }

}