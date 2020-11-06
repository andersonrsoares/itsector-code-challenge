package br.com.anderson.itsectorcodechallenge

import com.google.gson.Gson
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import kotlin.Throws
import kotlin.reflect.KClass

class MockJSONDataSourceRule(private val mGson: Gson) : TestRule {
    private var mValue: Any? = null

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(): T {
        return mValue as T
    }

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {

            override fun evaluate() {
                description?.getAnnotation(
                    MockJSONDataSource::class.java
                )?.let { jsonFileResource ->
                    val clazz: KClass<*> = jsonFileResource.clazz
                    val resourceName: String = jsonFileResource.fileName
                    val testClass = description.testClass
                    val inputStream: InputStream = testClass.classLoader?.getResourceAsStream(resourceName)
                        ?: error("Failed to load resource: $resourceName from $testClass")
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        mValue = mGson.fromJson(reader, clazz.java)
                    }
                    base.evaluate()
                }
            }
        }
    }
}
