package cz.lebedev.filesviewer.view.ui

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cz.lebedev.filesviewer.R
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.os.StrictMode




class MainActivity : AppCompatActivity(), HasSupportFragmentInjector{

    @Inject
    lateinit var dispatchingAndroidInjector : DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION: Int = 221

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        setContentView(R.layout.activity_main)
        if(savedInstanceState==null){
            setFragment();
        }
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
                R.id.action_settings -> preferenceFragment()
                R.id.action_refresh -> refresh()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun preferenceFragment(): Boolean {
        val directoryFragment = supportFragmentManager.findFragmentByTag(DirectoryFragment.TAG) as DirectoryFragment
        val directoryPreference = DirectoryPreference()
        val args = Bundle()
        args.putString(DirectoryPreference.TAG, directoryFragment.getActuallDir())
        directoryPreference.setArguments(args)
        supportFragmentManager.beginTransaction().addToBackStack(DirectoryPreference.TAG).replace(R.id.content, directoryPreference, null).commit()
        return true
    }

    private fun refresh(): Boolean {
        val directoryFragment = supportFragmentManager.findFragmentByTag(DirectoryFragment.TAG) as DirectoryFragment
        directoryFragment.refresh()
        return true
    }


    private fun setFragment() {
        val writeExternalStoragePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION
            )
        }else
            supportFragmentManager.beginTransaction().replace(R.id.content, DirectoryFragment(), DirectoryFragment.TAG).commit()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION)
        {
            val grantResultsLength = grantResults.size;
            if(grantResultsLength > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                setFragment()
            }else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.perm_denied), Toast.LENGTH_LONG).show();
            }
        }
    }


}
