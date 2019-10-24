require_relative  "base_counter"

class LimitedCounter < BaseCounter

  def initialize(start=0, limit=100)
    super(start)
    @limit = limit
    @num_inc = 0
  end

  def inc()
    if tally()+1 > @limit
      @count=@start
    end
    @num_inc+=1
    super()
  end

  def reset()
    super()
    @num_inc=0;
  end

  def num_inc()
    return @num_inc
  end

end


bc = LimitedCounter.new(0,2)
bc.inc().inc().inc()
puts bc
bc.reset()
bc.inc().inc()
puts bc

