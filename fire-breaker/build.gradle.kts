version = "1.0.0"

dependencies {
    implementation(project(":common"))
}

tasks.run.configure {
    args = listOf("5,5,A,*,L,*,D,*,A0,*,D0,*,L,*,+,*,L,*,C0,*,B0,*,C,*,L,*,B")
}
