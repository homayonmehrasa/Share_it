package ir.kurd.shareit.ui.music

import BlurBuilder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import ir.kurd.shareit.R
import ir.kurd.shareit.databinding.FragmentMusicPlayerBinding
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class MusicPlayerFragment : BaseFragment<MusicVM, FragmentMusicPlayerBinding>() {

   val mediaPlayer = MediaPlayer()
    lateinit var mHandler : Handler
    override val vm: MusicVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicPlayerBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun init() {

        onClicks ()
        playMusic()
        seekBarChange ()
        getBitmap ()

        binding.totalTimeLabel.text = createTimeLabel(mediaPlayer.duration)




        }

    private fun playMusic(){

        val uri = arguments?.getParcelable<Uri>("Uri")
        if (uri != null) {
            mediaPlayer.setDataSource(requireContext(), uri)
        }
        mediaPlayer.prepare()
        mediaPlayer.start()

        binding.positionBar.max = mediaPlayer.duration/1000
          mHandler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val mCurrentPosition: Int = mediaPlayer.currentPosition / 1000
                binding.positionBar.progress = mCurrentPosition
                binding.elapsedTimeLabel.text = createTimeLabel(mediaPlayer.currentPosition)
                mHandler.postDelayed(this, 1000)

            }
        }

        runnable.run()
        mediaPlayer.setOnCompletionListener {
            mHandler.removeCallbacks(runnable)
            binding.positionBar.progress=0
            binding.playBtn.setImageResource(R.drawable.ic_play)
        }

    }

    private fun onClicks (){
        binding.playBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                // Stop
                mediaPlayer.pause()
                binding.playBtn.setImageResource(R.drawable.ic_play)

            } else {
                // Start
                mediaPlayer.start()
                binding.playBtn.setImageResource(R.drawable.ic_pause)
            }
        }
        binding.forwardbtn.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition + 5000)
        }
        binding.backwardbtn.setOnClickListener {
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 5000)
        }



    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }

    private fun seekBarChange (){

        binding.positionBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * 1000)
                    binding.totalTimeLabel.text = createTimeLabel(mediaPlayer.duration)
                    binding.elapsedTimeLabel.text = createTimeLabel(mediaPlayer.currentPosition)
                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getBitmap () {
        val uri = arguments?.getParcelable<Uri>("Uri")
        val mmr = MediaMetadataRetriever()
        val rawArt: ByteArray?
        val art: Bitmap?
        val bfo = BitmapFactory.Options()

        mmr.setDataSource(requireContext(), uri)
        rawArt = mmr.embeddedPicture
        art = if (rawArt !=null) {
            BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, bfo)

        }else {
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_music)?.toBitmap()

        }

        binding.coverImg.setImageBitmap(art)

        val drawable: Drawable = BitmapDrawable(resources, art)
        val blurd = art?.let { if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            BlurBuilder.blur(requireContext() , it)
        } else {
            TODO("VERSION.SDK_INT < KITKAT")
        }
        }

        binding.mainLayout.background = BitmapDrawable(resources , blurd)

    }

}