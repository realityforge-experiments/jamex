package jamex.link;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.jms.Message;
import javax.jms.MessageListener;
import static org.junit.Assert.*;

final class MessageCollector
  implements MessageListener
{
  private static final int MAX_MESSAGE_COUNT = 10;

  private final LinkedBlockingQueue<Message> m_messages = new LinkedBlockingQueue<Message>( MAX_MESSAGE_COUNT );

  @Override
  public void onMessage( final Message message )
  {
    m_messages.add( message );
  }

  Collection<Message> expectMessageCount( final int expectedMessageCount,
                                          final long maxWait )
    throws InterruptedException
  {
    final LinkedList<Message> results = new LinkedList<Message>();
    final long start = System.currentTimeMillis();
    long now;
    while( results.size() < expectedMessageCount &&
           ( ( now = System.currentTimeMillis() ) < start + maxWait ) )
    {
      final long waitTime = Math.max( 1, start + maxWait - now );
      final Message message = m_messages.poll( waitTime, TimeUnit.MILLISECONDS );
      results.add( message );
    }

    assertEquals( "Expected message count", expectedMessageCount, results.size() );
    return results;
  }
}
