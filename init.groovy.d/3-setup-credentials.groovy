import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import hudson.plugins.sshslaves.*
import groovy.io.FileType
import java.nio.file.*

domain = Domain.global()
store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

def dir = new File("/credentials/git-keys")

dir.eachFileRecurse(FileType.FILES) { file ->
  if(file.name.endsWith(".pub"))
    return

  println "Adding SSH key from file ${file.path}..."

  privateKey = new BasicSSHUserPrivateKey(
    CredentialsScope.GLOBAL,
    file.name,
    "git",
    new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(file.path),
    "",
    ""
  )

  store.addCredentials(domain, privateKey)
}

dir = new File("/credentials/plain")

dir.eachFileRecurse(FileType.FILES) { file ->
  println "Adding plain credential from file ${file.path}..."

  def path = Paths.get(file.path)
  def secretBytes = SecretBytes.fromBytes(Files.readAllBytes(path))
  def credentials = new FileCredentialsImpl(CredentialsScope.GLOBAL, file.name, '', file.name, secretBytes)

  store.addCredentials(domain, credentials)
}
