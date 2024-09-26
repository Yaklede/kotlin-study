import java.util.concurrent.ThreadLocalRandom

const val NOT_EMPTY_CAR_NAME = "자동차 이름은 비어있을 수 없습니다."
const val NOT_BE_ZERO = "시도 횟수는 0보다 작을 수 없습니다."

fun main() {
    val names = CarInput().input()
    val tryCount = CountInput().input()

    val game = names makeGame tryCount
    game.play()

    println("최종 우승자: ${game.getWinner().joinToString(", ")}")
}

interface Input<In> {
    fun input(): In
}

class CarInput : Input<List<String>> {
    override fun input(): List<String> {
        println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        return readln().split(",").map(String::trim).also {
            require(it.all(String::isNotEmpty)) { NOT_EMPTY_CAR_NAME }
        }
    }
}

class CountInput : Input<Int> {
    override fun input(): Int {
        println("시도할 회수는 몇회인가요?")
        return readln().toInt().also {
            require(it > 0) { NOT_BE_ZERO }
        }
    }
}

infix fun List<String>.makeGame(tryCount: Int): Game {
    val cars = map { Car(it) }
    return Game(cars, tryCount)
}

class Game(
    private val cars: List<Car>,
    private val tryCount: Int
) {
    private var winners: List<String>? = null

    fun play() {
        repeat(tryCount) {
            cars.forEach { car ->
                car.run()
                println("${car.name}: ${"-".repeat(car.distance)}")
            }
            println()
        }

        val maxDistance = cars.maxOfOrNull { it.distance }
        winners = cars.filter { it.distance == maxDistance }.map(Car::name)
    }

    fun getWinner(): List<String> = winners ?: emptyList()
}

const val RANDOM_RANGE = 10

data class Car(
    val name: String,
    var distance: Int = 0
) {
    fun run() {
        if (ThreadLocalRandom.current().nextInt(RANDOM_RANGE) >= 4) {
            distance++
        }
    }
}

