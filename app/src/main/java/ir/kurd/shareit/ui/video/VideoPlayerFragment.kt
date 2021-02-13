package ir.kurd.shareit.ui.video

import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.VideoView
import ir.kurd.shareit.R
import ir.kurd.shareit.databinding.FragmentVideoPlayerBinding
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class VideoPlayerFragment : BaseFragment<VideoPlayerVM, FragmentVideoPlayerBinding> (){
    override val vm : VideoPlayerVM by viewModel()

    lateinit var video :VideoView
    lateinit var mHandler: Handler




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideoPlayerBinding.inflate(inflater)
        return binding.root

    }

    override fun init() {
        playVideo ()
        onClicks ()
        seekBarChange ()
        val duration = arguments?.getString("videoDuration")
        binding.totalTimeLabel.text =createTimeLabel (duration!!.toInt())

        binding.positionBar.max =duration.toInt()/1000

    }

    fun playVideo (){
        video= binding.VideoView



        val uri = arguments?.getParcelable<Uri>("videoUri")
        binding.VideoView.setVideoURI(uri)
        binding.VideoView.start()



        mHandler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val mCurrentPosition = binding.VideoView.currentPosition / 1000
                binding.positionBar.progress = mCurrentPosition
                binding.elapsedTimeLabel.text = createTimeLabel(binding.VideoView.currentPosition)
                mHandler.postDelayed(this, 1000)

            }
        }

        runnable.run()
        binding.VideoView.setOnCompletionListener {
            mHandler.removeCallbacks(runnable)
            binding.positionBar.progress=0
            binding.playBtn.setImageResource(R.drawable.ic_play)
        }


    }

    private fun onClicks (){
        binding.playBtn.setOnClickListener {
            if ( binding.VideoView.isPlaying) {
                // Stop
                binding.VideoView.pause()
                binding.playBtn.setImageResource(ir.kurd.shareit.R.drawable.ic_play)

            } else {
                // Start
                binding.VideoView.start()
                binding.playBtn.setImageResource(ir.kurd.shareit.R.drawable.ic_pause)
            }
        }
        binding.forwardbtn.setOnClickListener {
            binding.VideoView.seekTo( binding.VideoView.currentPosition + 5000)
        }
        binding.backwardbtn.setOnClickListener {
            binding.VideoView.seekTo( binding.VideoView.currentPosition - 5000)
        }



    }

    private fun seekBarChange (){

        binding.positionBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {

                    binding.VideoView.seekTo(progress*1000)
                    binding.elapsedTimeLabel.text = createTimeLabel( binding.VideoView.currentPosition)

                }
            }
        })

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

}
