enum class LevelEnum(val key: String) {
    BASIC("Básico"),
    INTERMEDIARY("Intermediário"),
    DIFFICULT("Difícil");

    fun printLevel() = ("nível ${key}")
}

class User(
        val name: String,
        val age: Int,
        var score: Int,
        val completedEducationalContent: MutableList<EducationalContent>,
        val completedCourses: MutableList<Course>
) {
    fun canSubscribeCourse(course: Course): Boolean {
        return !completedCourses.contains(course)
    }

    fun getMyScore() {
        println("Usuário ${name} com score ${score}")
    }
}

data class EducationalContent(var name: String, val duration: Int, val level: LevelEnum) {
    fun generateCertificate(user: User) {
        val alreadyFinish = user.completedEducationalContent.contains(this)

        if (alreadyFinish) {
            println("Certificado gerado para curso $name!✨\n")

            return
        }

        println("Necessário finalizar curso para gerar certificado!\n")
    }

    fun finishContent(user: User) {
        user.completedEducationalContent.add(this)

        when (this.level) {
            LevelEnum.BASIC -> user.score += 40
            LevelEnum.INTERMEDIARY -> user.score += 60
            LevelEnum.DIFFICULT -> user.score += 70
        }

        println("Parabéns ${user.name} - Curso $name finalizado!🔥\n")
    }
}

data class Course(val name: String, val contents: List<EducationalContent>, val level: LevelEnum) {
    
    val subscribers = mutableListOf<User>()
    
    fun register(user: User) {
        if (!user.canSubscribeCourse(this)) {
            println("Usuário já finalizou o curso! Baixe seu certificado.\n")

            return;
        }

        if (subscribers.contains(user)) {
            println("Usuário já cadastrado! Entre na área do curso.\n")

            return;
        }
        
        subscribers.add(user)

        println(this.getDescription())
    }

    fun generateCertificate(user: User) {
        val alreadyFinish = user.completedCourses.contains(this)

        if (alreadyFinish) {
            println("Parabéns ${user.name} - Certificado gerado para formação $name!✨\n")

            return
        }

        println("${user.name} é necessário finalizar curso para gerar certificado!\n")
    }

    fun finishContent(user: User) {
        user.completedCourses.add(this)

        when (this.level) {
            LevelEnum.BASIC -> user.score += 200
            LevelEnum.INTERMEDIARY -> user.score += 260
            LevelEnum.DIFFICULT -> user.score += 320
        }

        println("Parabéns ${user.name} - formação finalizada!🔥\n")
    }
}

fun <T> T.getDescription(): String {
    val defaultPhrase = "Parabéns por se matricular na formação";
    
    if (this is Course) {
        val castThis: Course = this;
        val courseHours = castThis.contents
            .stream()
            .map { item -> item.duration }
            .reduce { lastItem, item -> lastItem + item }
            .orElse(0)
        
        return "$defaultPhrase ${castThis.name} com carga horária de $courseHours min com ${castThis.level.printLevel()}.\n"
    }
    
    if (this is EducationalContent) {
        val castThis: EducationalContent = this;

        return "$defaultPhrase ${castThis.name} com carga horária de ${castThis.duration} min com ${castThis.level.printLevel()}.\n"
    }

    return "$defaultPhrase"
}

fun main() {
    val contentOne = EducationalContent("Learning Kotlin from Official Documentation", 40, LevelEnum.BASIC)
    val contentTwo = EducationalContent("How to Handling Exceptions", 60, LevelEnum.INTERMEDIARY)
    val contentThree = EducationalContent("Infix Functions in Kotlin", 80, LevelEnum.DIFFICULT)
    val listOfContent  = listOf(contentOne, contentTwo, contentThree)

    val kotlinCourse = Course("Kotlin Developer", listOfContent, LevelEnum.INTERMEDIARY)

    val userOne = User("Sandro Ramos", 24, 0, mutableListOf(), mutableListOf())
    val userTwo = User("Ludimlo da Silva", 19, 0, mutableListOf(), mutableListOf())
    val userThree = User("JRebel Roberto", 32, 0, mutableListOf(), mutableListOf())

    kotlinCourse.register(userOne)
    
    kotlinCourse.register(userTwo)
    
    kotlinCourse.register(userThree)
    
    //Try to register twice - No permission
    kotlinCourse.register(userOne)
    
    kotlinCourse.finishContent(userOne)
    
    //Try to register aafter finish course - No permission
    kotlinCourse.register(userOne)
    
    kotlinCourse.generateCertificate(userOne)

    //Try to gen certificate if not finish - No permission
    kotlinCourse.generateCertificate(userTwo)

    userOne.getMyScore()
    
}
