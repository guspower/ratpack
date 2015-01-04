/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package ratpack.launch

import static ratpack.launch.ServerConfigBuilder.DEFAULT_ENV_PREFIX

import spock.lang.Specification

class ServerConfigBuilderEnvVarsSpec extends Specification {

  ServerConfigBuilder builder
  Map<String, String> source

  def setup() {
    builder = ServerConfigBuilder.noBaseDir()
    source = [:]
  }

  def "set port"() {
    given:
    source['RATPACK_PORT'] = '5060'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.port == 5060
  }

  def "set property from custom prefix"() {
    given:
    source['APP_PORT'] = '6060'

    when:
    def config = builder.env('APP_', source).build()

    then:
    config.port == 6060
  }

  def "multiple sourcees override"() {
    given:
    source['RATPACK_PORT'] = '5060'
    source['APP_PORT'] = '8080'

    when:
    def config = builder.env('APP_', source).env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.port == 5060

    when:
    config = builder.env(DEFAULT_ENV_PREFIX, source).env('APP_', source).build()

    then:
    config.port == 8080
  }

  def "malformed port property throws exception"() {
    given:
    source['RATPACK_PORT'] = 'abcd'

    when:
    builder.env(DEFAULT_ENV_PREFIX, source)

    then:
    thrown NumberFormatException
  }

  def "set address"() {
    given:
    source['RATPACK_ADDRESS'] = 'localhost'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.address.hostName == 'localhost'
  }

  def "malformed address property throws exception"() {
    given:
    source['RATPACK_ADDRESS'] = 'blah'

    when:
    builder.env(DEFAULT_ENV_PREFIX, source)

    then:
    thrown RuntimeException
  }

  def "set development"() {
    given:
    source['RATPACK_DEVELOPMENT'] = 'true'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.development
  }

  def "non boolean development properties are false"() {
    given:
    source['RATPACK_DEVELOPMENT'] = 'hi'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    !config.development
  }

  def "set threads"() {
    given:
    source['RATPACK_THREADS'] = '10'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.threads == 10
  }

  def "malformed threads throws exception"() {
    given:
    source['RATPACK_THREADS'] = 'abcd'

    when:
    builder.env(DEFAULT_ENV_PREFIX, source)

    then:
    thrown NumberFormatException
  }

  def "set public address"() {
    given:
    source['RATPACK_PUBLIC_ADDRESS'] = 'http://ratpack.io'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.publicAddress.toString() == 'http://ratpack.io'
  }

  def "set max content length"() {
    given:
    source['RATPACK_MAX_CONTENT_LENGTH'] = '256'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.maxContentLength == 256
  }

  def "malformed max content length throws exception"() {
    given:
    source['RATPACK_MAX_CONTENT_LENGTH'] = 'abcd'

    when:
    builder.env(DEFAULT_ENV_PREFIX, source)

    then:
    thrown NumberFormatException
  }

  def "set time responses"() {
    given:
    source['RATPACK_TIME_RESPONSES'] = 'true'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.timeResponses
  }

  def "none boolean time responses are false"() {
    given:
    source['RATPACK_TIME_RESPONSES'] = 'abcd'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    !config.timeResponses
  }

  def "set compress responses"() {
    given:
    source['RATPACK_COMPRESS_RESPONSES'] = 'true'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.compressResponses
  }

  def "none boolean compressResponses responses are false"() {
    given:
    source['RATPACK_COMPRESS_RESPONSES'] = 'abcd'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    !config.compressResponses
  }

  def "set compression min size"() {
    given:
    source['RATPACK_COMPRESSION_MIN_SIZE'] = '256'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.compressionMinSize == 256L
  }

  def "malformed compress min size throws exception"() {
    given:
    source['RATPACK_COMPRESSION_MIN_SIZE'] = 'abcd'

    when:
    builder.env(DEFAULT_ENV_PREFIX, source)

    then:
    thrown NumberFormatException
  }

  def "set compression white list"() {
    given:
    source['RATPACK_COMPRESSION_WHITE_LIST_MIME_TYPES'] = 'json,xml'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.compressionMimeTypeWhiteList == ['json', 'xml'] as Set
  }

  def "set compression black list"() {
    given:
    source['RATPACK_COMPRESSION_BLACK_LIST_MIME_TYPES'] = 'json,xml'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.compressionMimeTypeBlackList == ['json', 'xml'] as Set
  }

  def "set index files"() {
    given:
    source['RATPACK_INDEX_FILES'] = 'home.html,index.html'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.indexFiles == ['home.html', 'index.html']
  }

  def "trim white space in comma separated lists"() {
    given:
    source['RATPACK_INDEX_FILES'] = 'home.html , index.html'

    when:
    def config = builder.env(DEFAULT_ENV_PREFIX, source).build()

    then:
    config.indexFiles == ['home.html', 'index.html']
  }
}
