require_relative 'counter'
class BaseCounter

  include Counter

  def initialize(start=0)
    @start=start
    @count=start
  end

  def start()
    return @start
  end

  def inc()
    @count+=1
    return self
  end

  def tally()
    return @count
  end

  def reset()
    @count=@start
  end

end

bc = BaseCounter.new(4)
bc.inc().inc().inc()
puts bc
bc.reset()
bc.inc().inc()
puts bc
