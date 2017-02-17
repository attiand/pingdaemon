import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Host {
  def address

  String getName() {
    def inet = InetAddress.getByName(address)
    return inet ? inet.getCanonicalHostName() : '-'
  }
}
