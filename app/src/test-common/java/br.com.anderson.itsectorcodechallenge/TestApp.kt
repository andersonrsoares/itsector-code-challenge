package br.com.anderson.itsectorcodechallenge

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 */
class TestApp : Application()
