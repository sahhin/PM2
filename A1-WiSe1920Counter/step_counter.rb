require_relative  "base_counter"
class StepCounter < BaseCounter

  def initialize(start=0, step=1)
    super(start)
    @step = step
  end

  def inc()
    @count+=@step
    return self
  end

  def num_inc()
    super()/@step
  end

end

bc = StepCounter.new(4,2)
bc.inc().inc().inc()
puts bc
bc.reset()
bc.inc().inc()
puts bc

